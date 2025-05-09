package com.zerobase.reservation.dto.reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequest {

    private Long userId;
    private Long storeId;
    private LocalDateTime reservationTime;
}
