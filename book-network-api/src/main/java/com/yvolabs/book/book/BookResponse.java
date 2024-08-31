package com.yvolabs.book.book;

import lombok.*;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 30/08/2024
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String owner;
    private byte[] cover;
    private double rate;
    private boolean archived;
    private boolean shareable;

}