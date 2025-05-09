package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser_Id(Long userId);
    List<Reservation> findByStore_Id(Long storeId);
}
