package com.yvolabs.book.feedback;

import com.yvolabs.book.book.Book;
import com.yvolabs.book.book.BookRepository;
import com.yvolabs.book.exception.OperationNotPermittedException;
import com.yvolabs.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 02/09/2024
 */

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public Integer saveFeedback(FeedbackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID: " + request.bookId()));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You cannot give feedback for an archived or not shareable");
        }

        User user = (User) connectedUser.getPrincipal();
        if (userOwnsBook(book, user)) {
            throw new OperationNotPermittedException("You cannot give feedback to your own book");
        }

        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }

    private static boolean userOwnsBook(Book book, User user) {
        return Objects.equals(book.getOwner().getId(), user.getId());
    }
}
