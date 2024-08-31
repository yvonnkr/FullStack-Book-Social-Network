package com.yvolabs.book.book;

import com.yvolabs.book.common.PageResponse;
import org.springframework.security.core.Authentication;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 30/08/2024
 */
public interface BookService {
    Integer save(BookRequest request, Authentication connectedUser);

    BookResponse findById(Integer bookId);

    PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser);

    PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser);

}
