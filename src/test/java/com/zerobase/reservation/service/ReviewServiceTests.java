package com.zerobase.reservation.service;


import com.zerobase.reservation.domain.*;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.repository.ReviewRepository;
import com.zerobase.reservation.repository.UserRepository;
import com.zerobase.reservation.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test // 방문 확인된 주문이 아닌데 리뷰를 작성하려는 경우
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

    // 리뷰 수정 기능 Test : 작성자만 가능

    // 성공

    @Test // 리뷰 생성 기능 Test
    void testUpdateReview_success_byUser() {
        User author = User.builder().id(1L).build();

        Review review = Review.builder()
                .id(1L)
                .user(author)
                .content("Old content")
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Review result = reviewService.updateReview(1L, 1L, "Updated content");

        assertEquals("Updated content", result.getContent());
    }

    @Test // 리뷰 작성하지 않은 사람이 리뷰를 건드려고 할 때 AccessDeniedException을 예상하는 Test
    void testUpdateReview_fail_notAuthor() {
        User author = User.builder().id(1L).build();
        Review review = Review.builder()
                .id(1L)
                .user(author)
                .content("Old content")
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThrows(AccessDeniedException.class, () ->
                reviewService.updateReview(1L, 2L, "Hacked update"));
    }

    // === 리뷰 삭제: 작성자 or 점장 ===
    @Test // 리뷰 작성자에 의해 리뷰가 삭제되는 기능 Test
    void testDeleteReview_success_byAuthor() {
        User author = User.builder().id(1L).build();
        Store store = Store.builder().partner(Partner.builder().id(1L).build()).build();
        Reservation reservation = Reservation.builder().user(author).store(store).build();
        Review review = Review.builder().id(1L).user(author).reservation(reservation).build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertDoesNotThrow(() -> reviewService.deleteReview(1L, 1L));
    }

    @Test // 점장에 의해 리뷰가 삭제되는 기능 Test
    void testDeleteReview_success_byOwner() {
        User author = User.builder().id(1L).build();
        Partner owner = Partner.builder().id(2L).build();
        Store store = Store.builder().partner(owner).build();
        Reservation reservation = Reservation.builder().user(author).store(store).build();
        Review review = Review.builder().id(1L).user(author).reservation(reservation).build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertDoesNotThrow(() -> reviewService.deleteReview(1L, 2L));
    }

    @Test // 리뷰 작성자도, 그 매당의 점장도 아닌 경우
    void testDeleteReview_fail_unauthorizedUser() {
        User author = User.builder().id(1L).build();
        Partner owner = Partner.builder().id(2L).build();
        Store store = Store.builder().partner(owner).build();
        Reservation reservation = Reservation.builder().user(author).store(store).build();
        Review review = Review.builder().id(1L).user(author).reservation(reservation).build();

        // 리뷰를 조회하는 부분만 스텁 처리
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // 실제 deleteReview 호출 시 AccessDeniedException을 예상.
        assertThrows(AccessDeniedException.class, () -> reviewService.deleteReview(1L, 3L));
    }



}
