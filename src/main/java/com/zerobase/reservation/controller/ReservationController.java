package com.zerobase.reservation.controller;

import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.dto.reservation.ReservationRequest;
import com.zerobase.reservation.dto.reservation.ReservationResponse;
import com.zerobase.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Tag(name = "Reservation API", description = "예약 관련 기능")
class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예약 생성", description = "사용자가 새로운 예약을 생성합니다.")
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    @Operation(summary = "사용자 예약 조회", description = "특정 사용자의 예약 내역을 조회합니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUser(userId));
    }

    @Operation(summary = "매장 예약 조회", description = "특정 매장의 예약 목록을 조회합니다. (점장용)")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<Reservation>> getReservationsByStore(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(reservationService.getReservationsByStore(storeId));
    }

    @Operation(summary = "예약 응답", description = "점장이 예약을 승인 또는 거절합니다.")
    @PatchMapping("/{reservationId}/respond")
    public ResponseEntity<Reservation> respondToReservation(
            @PathVariable("reservationId") Long reservationId,
            @RequestParam("partnerId") Long partnerId,
            @RequestParam("approved") boolean approved
    ) {
        return ResponseEntity.ok(reservationService.respondToReservation(reservationId, partnerId, approved));
    }

    @Operation(summary = "방문 확인", description = "매장 키오스크에서 예약자의 방문을 확인합니다.")
    @PostMapping("/{reservationId}/confirm")
    public ResponseEntity<Reservation> confirmVisit(@PathVariable("reservationId") Long reservationId) {
        return ResponseEntity.ok(reservationService.confirmVisit(reservationId));
    }
}


