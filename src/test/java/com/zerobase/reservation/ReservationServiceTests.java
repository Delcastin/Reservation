package com.zerobase.reservation;


import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.domain.ReservationStatus;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.domain.User;
import com.zerobase.reservation.dto.reservation.ReservationRequest;
import com.zerobase.reservation.dto.reservation.ReservationResponse;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.repository.UserRepository;
import com.zerobase.reservation.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTests {


    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private User user;
    private Store store;

    Reservation reservation = Reservation.builder()
            .user(user)
            .store(store)
            .reservationDate(LocalDateTime.of(2025, 1, 1, 8, 32))
            .status(ReservationStatus.PENDING)
            .build();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .name("Test")
                .build();

        store = Store.builder()
                .id(1L)
                .name("Test Store")
                .address("Test Address")
                .build();

        reservation = Reservation.builder()
                .id(1L)
                .store(store)
                .user(user)
                .reservationDate(reservation.getReservationDate())
                .build();
    }

    @Test // 단순 예약 생성 Test
    void testReserve(){

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation savedReservation = reservationService.reserve(reservation);

        assertEquals(savedReservation.getUser().getId(), user.getId());
        assertEquals(savedReservation.getStore().getId(), store.getId());
    }

    @Test // 사용자ID로 예약 목록 조회할 수 있는지 Test
    void testGetReservationByUser(){

        when(reservationRepository.findByUser_Id(1L)).thenReturn(List.of(reservation));

        List<Reservation> reservations = reservationService.getReservationsByUser(1L);

        assertEquals(1, reservations.size());
        assertEquals(reservations.get(0).getUser().getId(), user.getId());
    }

    @Test // 특정 매장 ID로 예약 목록 조회할 수 있는지 Test
    void testGetReservationByStore(){

        when(reservationRepository.findByStore_Id(1L)).thenReturn(List.of(reservation));

        List<Reservation> reservations = reservationService.getReservationsByStore(1L);

        assertEquals(1, reservations.size());
        assertEquals(reservations.get(0).getStore().getId(), store.getId());
    }

    @Test // ReservationRequest을 이용해서 예약을 생성하는 로직을 검증하는 Test
    void testCreateReservation(){

        ReservationRequest request = ReservationRequest.builder()
                .userId(1L)
                .storeId(1L)
                .reservationTime(reservation.getReservationDate())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(reservationRepository.save(ArgumentMatchers.<Reservation>any())).thenReturn(reservation);

        ReservationResponse response = reservationService.createReservation(request);

        assertNotNull(response);
        assertEquals(response.getStoreName(), store.getName());
        assertEquals(response.getUserName(), user.getName());
    }

    @Test // 해당 유저가 해당 매장에서 특정 시간에 이미 예약했는지 여부 확인하는 Test
    void testExistsByReservationDetails(){

        ReservationRequest request = ReservationRequest.builder()
                .userId(1L)
                .storeId(1L)
                .reservationTime(reservation.getReservationDate())
                .build();

        when(reservationRepository.existsByStoreIdAndUserIdAndReservationDate(1L, 1L, reservation.getReservationDate())).thenReturn(Boolean.TRUE);

        boolean exists = reservationService.existsByReservationDetails(request);

        assertTrue(exists);

    }

    // 방문 확인 기능 Test

    @Test
    void testConfirmVisit_Success(){
        Long reservationId = 1L;
        LocalDateTime reservationTime = LocalDateTime.now();

        Reservation reservation = Reservation.builder()
                .id(reservationId)
                .reservationDate(reservationTime)
                .user(user)
                .status(ReservationStatus.PENDING)
                .build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(ArgumentMatchers.<Reservation>any())).thenReturn(reservation);

        Reservation result = reservationService.confirmVisit(reservationId);

        assertEquals(ReservationStatus.CONFIRMED, result.getStatus());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void testConfirmVisit_ReservationNotFound(){
        Long reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> reservationService.confirmVisit(reservationId));
    }

    @Test
    void testConfirmVisit_AlreadyVisited(){
        Long reservationId = 1L;

        Reservation reservation = Reservation.builder()
                .id(reservationId)
                .reservationDate(LocalDateTime.now())
                .user(user)
                .status(ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalArgumentException.class, () -> reservationService.confirmVisit(reservationId));
    }

    @Test
    void testConfirmVisit_OutsideAllowedTime(){
        Long reservationId = 1L;

        LocalDateTime earlyTime = LocalDateTime.now().plusHours(1);

        Reservation reservation = Reservation.builder()
                .id(reservationId)
                .reservationDate(earlyTime)
                .user(user)
                .status(ReservationStatus.PENDING)
                .build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalArgumentException.class, () -> reservationService.confirmVisit(reservationId));
    }


}
