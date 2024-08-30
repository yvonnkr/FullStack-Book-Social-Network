package com.yvolabs.book.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 30/08/2024
 *
 * @apiNote Validation Errors will be numbered based on the attribute and the Error message will be set by client/frontend
 */
public record BookRequest(

        Integer id,

        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String title,

        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String authorName,

        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String isbn,

        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String synopsis,

        boolean shareable
) {
}
