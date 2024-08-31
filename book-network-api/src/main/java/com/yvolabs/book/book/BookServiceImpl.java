package com.yvolabs.book.book;

import com.yvolabs.book.common.PageResponse;
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

}
