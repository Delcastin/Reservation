package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.ReservationStatus;
import com.zerobase.reservation.domain.Review;

import java.time.LocalDateTime;

public interface ReviewService {

    Review writeReview(Long reservationId, Long userId, String content);

    Review updateReview(Long reviewId, Long userId, String content);

    void deleteReview(Long reviewId, Long userId);
}
