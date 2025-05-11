package com.zerobase.reservation;


import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.domain.ReservationStatus;
import com.zerobase.reservation.domain.Review;
import com.zerobase.reservation.domain.User;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.repository.ReviewRepository;
import com.zerobase.reservation.repository.UserRepository;
import com.zerobase.reservation.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void testWriteReview_success() {
        Long userId = 1L;
        Long reservationId = 1L;

        User user = User.builder().id(userId).name("테스터").build();
        Reservation reservation = Reservation.builder()
                .id(reservationId)
                .user(user)
                .status(ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Review review = reviewService.writeReview(reservationId, userId, "좋아요!");

        assertEquals("좋아요!", review.getContent());
        assertEquals(user, review.getReservation().getUser());
        assertEquals(reservation, review.getReservation());
    }

    @Test
    void testWriteReview_fail_NotConfirmed() {
        User user = User.builder()
                .id(2L)
                .name("Test User")
                .email("test@test.com")
                .build();

        Long reservationId = 2L;
        Reservation reservation = Reservation.builder()
                .id(reservationId)
                .status(ReservationStatus.PENDING)
                .user(user)
                .build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        lenient().when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () ->
                reviewService.writeReview(reservationId, 2L,"별로였어요")
        );
    }


}
