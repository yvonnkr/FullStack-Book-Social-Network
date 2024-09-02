package com.yvolabs.book.feedback;

import com.yvolabs.book.common.PageResponse;
import org.springframework.security.core.Authentication;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 02/09/2024
 */
public interface FeedbackService {
    Integer saveFeedback(FeedbackRequest request, Authentication connectedUser);

    PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser);
}
