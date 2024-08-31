package com.yvolabs.book.book;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 31/08/2024
 * @apiNote We define some specification then can use them in repo to create a query and can combine the specs using logical operators(and,or etc)
 *
 * @apiNote criteriaBuilder.like(), There are 2 wildcards often used in conjunction with the .like() method, to check if provided is contained in the value
 *             % = represents zero, one or multiple characters
 *             _ = represents one, single character
 */

public class BookSpecification {

    public static Specification<Book> withOwnerId(Integer ownerId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId));
    }
}
