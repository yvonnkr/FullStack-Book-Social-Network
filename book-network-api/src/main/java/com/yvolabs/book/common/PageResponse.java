package com.yvolabs.book.common;

import lombok.*;

import java.util.List;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 30/08/2024
 *
 * This is a custom PageResponse, an alternative/wrapper to the Page.Class from spring data jpa which has alot of details that we don't need
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
