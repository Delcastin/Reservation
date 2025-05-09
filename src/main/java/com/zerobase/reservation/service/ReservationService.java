package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.dto.reservation.ReservationRequest;
import com.zerobase.reservation.dto.reservation.ReservationResponse;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Reservation reserve(Reservation reservation);

    List<Reservation> getReservationsByUser(Long userId);

    List<Reservation> getReservationsByStore(Long storeId);

    Optional<Reservation> findById(Long id);

    List<ReservationResponse> getAllReservations();

    Optional<Reservation> getReservationById(Long id);

    boolean existsByReservationDetails(ReservationRequest reservationRequest);

    ReservationResponse createReservation(ReservationRequest reservationRequest);
}
