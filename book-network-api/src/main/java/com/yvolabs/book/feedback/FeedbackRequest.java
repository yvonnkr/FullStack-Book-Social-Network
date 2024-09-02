package com.yvolabs.book.feedback;

import jakarta.validation.constraints.*;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 02/09/2024
 */

public record FeedbackRequest(
        @NotNull(message = "200")
        @Positive(message = "200")
        @Min(value = 0, message = "201")
        @Max(value = 5, message = "202")
        Double note,

        @NotNull(message = "203")
        @NotEmpty(message = "203")
        @NotBlank(message = "203")
        String comment,

        @NotNull(message = "204")
        Integer bookId
) {
}
