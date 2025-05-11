package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndUserId(Long reviewId, Long userId);

    List<Review> findByReservationId(Long reservationId);
}
