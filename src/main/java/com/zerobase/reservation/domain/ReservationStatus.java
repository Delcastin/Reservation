package com.zerobase.reservation.domain;

public enum ReservationStatus {
    PENDING, // 대기 중
    CONFIRMED,  // 예약 완료
    CANCELED,  // 예약 취소
    APPROVED, // 점장 승인
    REJECTED // 예약 거절
}
