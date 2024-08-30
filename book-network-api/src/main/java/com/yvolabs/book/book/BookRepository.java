package com.yvolabs.book.book;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 30/08/2024
 */
public interface BookRepository extends JpaRepository<Book, Integer> {
}
