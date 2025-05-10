package com.zerobase.reservation.service.impl;

import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.domain.User;
import com.zerobase.reservation.dto.reservation.ReservationRequest;
import com.zerobase.reservation.dto.reservation.ReservationResponse;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.repository.UserRepository;
import com.zerobase.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Override
    public Reservation reserve(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUser_Id(userId);
    }

    @Override
    public List<Reservation> getReservationsByStore(Long storeId) {
        return reservationRepository.findByStore_Id(storeId);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return List.of();
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public boolean existsByReservationDetails(ReservationRequest reservationRequest) {
        // 예약 중복 확인: 동일한 매장, 사용자, 예약 시간에 대해 예약이 존재하는지 확인
        return reservationRepository.existsByStoreIdAndUserIdAndReservationTime(
                reservationRequest.getStoreId(),
                reservationRequest.getUserId(),
                reservationRequest.getReservationTime()  // reservationTime 사용
        );
    }


    @Override
    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        // User와 Store 객체를 조회
        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        Store store = storeRepository.findById(reservationRequest.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 매장을 찾을 수 없습니다."));

        // Reservation 객체 생성
        Reservation reservation = Reservation.builder()
                .user(user)  // User 객체 설정
                .store(store)  // Store 객체 설정
                .reservationDate(reservationRequest.getReservationTime())  // reservationTime 설정
                .build();

        // 예약 저장
        reservationRepository.save(reservation);

        // ReservationResponse 반환
        return ReservationResponse.fromReservation(reservation);
    }


}
