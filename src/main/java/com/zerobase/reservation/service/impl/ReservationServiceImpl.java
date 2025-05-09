package com.zerobase.reservation.service.impl;

import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.dto.reservation.ReservationRequest;
import com.zerobase.reservation.dto.reservation.ReservationResponse;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

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

        // 예약 중복 확인 로직
        return false;
    }

    @Override
    public ReservationResponse createReservation(ReservationRequest reservationRequest) {

        Reservation reservation = new Reservation();

        reservationRepository.save(reservation);

        return ReservationResponse.fromReservation(reservation);
    }
}
