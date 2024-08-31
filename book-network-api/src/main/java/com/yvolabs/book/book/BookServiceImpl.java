package com.yvolabs.book.book;

import com.yvolabs.book.common.PageResponse;
import com.yvolabs.book.exception.OperationNotPermittedException;
import com.yvolabs.book.history.BookTransactionHistory;
import com.yvolabs.book.history.BookTransactionHistoryRepository;
import com.yvolabs.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 30/08/2024
 */

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final BookRepository repository;
    private final BookTransactionHistoryRepository transactionHistoryRepository;

    @Override
    public Integer save(BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return repository.save(book).getId();
    }

    @Override
    public BookResponse findById(Integer bookId) {
        return repository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No Book Found with ID: " + bookId));

    }

    @Override
    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> booksPage = repository.findAllDisplayableBooks(pageable, user.getId());

        List<BookResponse> booksResponse = booksPage.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                booksResponse,
                booksPage.getNumber(),
                booksPage.getSize(),
                booksPage.getTotalElements(),
                booksPage.getTotalPages(),
                booksPage.isFirst(),
                booksPage.isLast()
        );

    }

    @Override
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        //Query using specification criteria
        Specification<Book> spec = BookSpecification.withOwnerId(user.getId());
        Page<Book> booksPage = repository.findAll(spec, pageable);

        List<BookResponse> booksResponse = booksPage.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                booksResponse,
                booksPage.getNumber(),
                booksPage.getSize(),
                booksPage.getTotalElements(),
                booksPage.getTotalPages(),
                booksPage.isFirst(),
                booksPage.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());

        List<BorrowedBookResponse> booksResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                booksResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allReturnedBooks = transactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> booksResponse = allReturnedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                booksResponse,
                allReturnedBooks.getNumber(),
                allReturnedBooks.getSize(),
                allReturnedBooks.getTotalElements(),
                allReturnedBooks.getTotalPages(),
                allReturnedBooks.isFirst(),
                allReturnedBooks.isLast()
        );
    }

    @Override
    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());

        Book book = repository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No Book Found with ID: " + bookId));

        if (userDoesNotOwnBook(book, user)) {
            throw new OperationNotPermittedException("You cannot update other users book's shareable status");
        }

        book.setShareable(!book.isShareable());
        repository.save(book);
        return bookId;
    }

    @Override
    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());

        Book book = repository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No Book Found with ID: " + bookId));

        if (userDoesNotOwnBook(book, user)) {
            throw new OperationNotPermittedException("You cannot update other users book's archived status");
        }

        book.setArchived(!book.isArchived());
        repository.save(book);
        return bookId;
    }

    private static boolean userDoesNotOwnBook(Book book, User user) {
        return !Objects.equals(book.getOwner().getId(), user.getId());
    }

}
