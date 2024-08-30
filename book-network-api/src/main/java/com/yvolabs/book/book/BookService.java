package com.yvolabs.book.book;

import org.springframework.security.core.Authentication;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 30/08/2024
 */
public interface BookService {
    Integer save(BookRequest request, Authentication connectedUser);
}
