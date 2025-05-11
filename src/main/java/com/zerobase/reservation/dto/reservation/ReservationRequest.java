package com.zerobase.reservation.dto.reservation;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {

    private Long userId;
    private Long storeId;
    private LocalDateTime reservationTime;
}
