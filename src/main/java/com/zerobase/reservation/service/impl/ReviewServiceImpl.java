package com.zerobase.reservation.service.impl;

import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.domain.ReservationStatus;
import com.zerobase.reservation.domain.Review;
import com.zerobase.reservation.domain.User;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.repository.ReviewRepository;
import com.zerobase.reservation.repository.UserRepository;
import com.zerobase.reservation.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public Review writeReview(Long reservationId, Long userId, String content) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("예약을 찾을 수 없습니다."));

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("방문 확인된 예약만 리뷰 작성이 가능합니다.");
        }

        if (!reservation.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 예약만 리뷰를 작성할 수 있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Review review = Review.builder()
                .reservation(reservation)
                .user(user)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Long reviewId, Long userId, String content) {
        Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new AccessDeniedException("본인의 리뷰만 수정할 수 있습니다."));

        review.setContent(content);
        review.setUpdatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new AccessDeniedException("본인의 리뷰만 삭제할 수 있습니다."));

        reviewRepository.delete(review);
    }
}
