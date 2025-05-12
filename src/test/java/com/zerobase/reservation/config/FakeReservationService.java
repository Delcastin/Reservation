package com.zerobase.reservation.config;

import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.domain.ReservationStatus;
import com.zerobase.reservation.dto.reservation.ReservationRequest;
import com.zerobase.reservation.dto.reservation.ReservationResponse;
import com.zerobase.reservation.service.ReservationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeReservationService implements ReservationService {

    private List<Reservation> reservations = new ArrayList<>();

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


    @Override
    public Reservation reserve(Reservation reservation) {
        return null;
    }

    @Override
    public List<Reservation> getReservationsByUser(Long userId) {
        return reservations.stream()
                .filter(r -> r.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getReservationsByStore(Long storeId) {
        return reservations.stream()
                .filter(r -> r.getStore().getId().equals(storeId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return List.of();
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean existsByReservationDetails(ReservationRequest reservationRequest) {
        return false;
    }

    @Override
    public ReservationResponse createReservation(ReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservationDate(request.getReservationTime());
        // user, store 등은 간단한 dummy 객체로 채움 (생략 가능)
        reservations.add(reservation);

        return ReservationResponse.builder()
                .id(reservation.getId())
                .userId(request.getUserId())
                .storeId(request.getStoreId())
                .userName("Test User")
                .storeName("Test Store")
                .reservationTime(request.getReservationTime())
                .build();
    }

    @Override
    public Reservation confirmVisit(Long reservationId) {
        for (Reservation res : reservations) {
            if (res.getId().equals(reservationId)) {
                res.setStatus(ReservationStatus.CONFIRMED);  // 방문 처리
                return res;
            }
        }
        return null;
    }

    @Override
    public Reservation respondToReservation(Long reservationId, Long partnerId, boolean approved) {
        Reservation res = reservations.stream()
                .filter(r -> r.getId().equals(reservationId))
                .findFirst()
                .orElseThrow();
        res.setStatus(approved ? ReservationStatus.APPROVED : ReservationStatus.REJECTED);
        return res;
    }
}
