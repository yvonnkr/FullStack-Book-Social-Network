package com.yvolabs.book.feedback;

import org.springframework.security.core.Authentication;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 02/09/2024
 */
public interface FeedbackService {
    Integer saveFeedback(FeedbackRequest request, Authentication connectedUser);
}
