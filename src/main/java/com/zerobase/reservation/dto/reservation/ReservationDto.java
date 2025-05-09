package com.zerobase.reservation.dto.reservation;


import com.zerobase.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Long id;
    private Long userId;
    private Long storeId;
    private LocalDateTime reservationTime;
    private String status;

    public static ReservationResponse fromReservation(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .storeId(reservation.getStore().getId())
                .storeName(reservation.getStore().getName())
                .userName(reservation.getUser().getName())
                .reservationTime(reservation.getReservationDate())
                .build();
    }
}
