package com.zerobase.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.config.FakeReservationService;
import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.domain.ReservationStatus;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.domain.User;
import com.zerobase.reservation.dto.reservation.ReservationRequest;
import com.zerobase.reservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ReservationController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@Import(FakeReservationService.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ReservationService reservationService;

    @Test
    void testCreateReservation() throws Exception {
        ReservationRequest request = new ReservationRequest(1L, 2L, LocalDateTime.now().plusDays(1));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservationsByUser() throws Exception {
        mockMvc.perform(get("/reservations/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservationsByStore() throws Exception {
        mockMvc.perform(get("/reservations/store/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testRespondToReservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setStore(Store.builder().id(2L).name("Store").build());
        reservation.setUser(User.builder().id(2L).name("User").build());

        ((FakeReservationService) reservationService).setReservations(List.of(reservation));

        mockMvc.perform(patch("/reservations/1/respond")
                        .param("partnerId", "1")
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }


    @Test
    void testConfirmVisit() throws Exception {
        mockMvc.perform((RequestBuilder) post("/reservations/1/confirm"))
                .andExpect(status().isOk());
    }
}
