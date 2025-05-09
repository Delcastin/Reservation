package com.zerobase.reservation.dto.reservation;

import com.zerobase.reservation.domain.Reservation;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationResponse {

    private Long id;
    private Long userId;
    private Long storeId;
    private String storeName;
    private String userName;
    private LocalDateTime reservationTime;

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
