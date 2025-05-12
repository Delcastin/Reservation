package com.zerobase.reservation.service.impl;

import com.zerobase.reservation.domain.*;
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

    @Override // 리뷰 작성 기능
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

    @Override // 리뷰 수정 기능
    public Review updateReview(Long reviewId, Long userId, String content) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰가 존재하지 않습니다."));
        
        if(!review.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("리뷰 작성자만 수정이 가능합니다.");
        }
        
        review.setContent(content);
        review.setUpdatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    @Override // 리뷰 삭제 기능
    public void deleteReview(Long reviewId, Long requesterId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

        Reservation reservation = review.getReservation();
        Store store = reservation.getStore();
        Partner partner = store.getPartner();

        boolean isWriter = review.getUser().getId().equals(requesterId);
        boolean isStoreOwner = partner.getId().equals(requesterId);

        if(!isWriter && !isStoreOwner) {
            throw new AccessDeniedException("리뷰 삭제는 작성자 또는 당 매장의 점장만 가능합니다.");
        }

        reviewRepository.delete(review);
    }
}
