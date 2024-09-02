package com.yvolabs.book.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 02/09/2024
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
