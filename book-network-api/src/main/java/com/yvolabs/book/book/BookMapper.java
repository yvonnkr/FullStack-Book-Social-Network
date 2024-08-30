package com.yvolabs.book.book;

import org.springframework.stereotype.Component;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 30/08/2024
 */

@Component
public class BookMapper {
    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }
}
