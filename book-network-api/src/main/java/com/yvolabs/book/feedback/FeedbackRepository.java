package com.yvolabs.book.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 02/09/2024
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    @Query("""
                        SELECT feedback
                        FROM Feedback  feedback
                        WHERE feedback.book.id = :bookId
            """)
    Page<Feedback> findAllByBookId(Integer bookId, Pageable pageable);
}
