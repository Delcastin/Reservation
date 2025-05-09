package com.zerobase.reservation.controller;


import com.zerobase.reservation.domain.Partner;
import com.zerobase.reservation.dto.partner.PartnerRegisterRequest;
import com.zerobase.reservation.dto.partner.PartnerResponse;
import com.zerobase.reservation.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping("/{id}")
    public ResponseEntity<Partner> getPartner(@PathVariable Long id) {
        Partner partner = partnerService.findById(id);  // 존재하지 않으면 예외 발생
        return ResponseEntity.ok(partner);
    }

    @PostMapping("/register")
    public ResponseEntity<PartnerResponse> registerPartner(@RequestBody PartnerRegisterRequest request) {

        Partner partner = Partner.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .build();

        Partner saved = partnerService.registerPartner(partner);

        PartnerResponse response = PartnerResponse.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .name(saved.getName())
                .build();

        return ResponseEntity.ok(response);
    }

}
