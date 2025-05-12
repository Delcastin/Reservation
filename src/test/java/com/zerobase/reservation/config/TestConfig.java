package com.zerobase.reservation.config;


import com.zerobase.reservation.service.ReservationService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public ReservationService reservationService() {
        return new FakeReservationService();
    }
}
