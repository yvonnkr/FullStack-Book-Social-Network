package com.yvolabs.book.book;

import com.yvolabs.book.common.BaseEntity;
import com.yvolabs.book.feedback.Feedback;
import com.yvolabs.book.history.BookTransactionHistory;
import com.yvolabs.book.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 29/08/2024
 */

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }

        double rate = feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);

        // Return 4.0 if roundedRate is less than 4.5, otherwise return 4.5
        double roundedRate = Math.round(rate * 10.0) / 10.0;
        log.info("AverageRate = {}, RoundedAverageRate = {}", rate, roundedRate);
        return roundedRate;
    }
}
