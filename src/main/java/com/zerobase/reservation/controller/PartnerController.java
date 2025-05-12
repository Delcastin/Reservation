package com.zerobase.reservation.controller;


import com.zerobase.reservation.domain.Partner;
import com.zerobase.reservation.dto.partner.PartnerDto;
import com.zerobase.reservation.dto.partner.PartnerRegisterRequest;
import com.zerobase.reservation.dto.partner.PartnerResponse;
import com.zerobase.reservation.service.PartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/partners")
@Tag(name = "Partner API", description = "점장 관련 기능")
public class PartnerController {

    private final PartnerService partnerService;

    @Operation(summary = "점장 로그인", description = "이메일과 비밀번호로 로그인")
    @GetMapping("/login")
    public ResponseEntity<Partner> getPartner(@RequestBody PartnerRegisterRequest request) {
        Partner partner = partnerService.login(request);  // 존재하지 않으면 예외 발생
        return ResponseEntity.ok(partner);
    }


    @Operation(summary = "점장 파트너쉽 회원가입", description = "새로운 파트너를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<Partner> registerPartner(@RequestBody PartnerRegisterRequest request) {

        Partner partner = Partner.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(partnerService.encodePassword(request.getPassword()))
                .phone(request.getPhone())
                .build();
        return ResponseEntity.ok(partnerService.registerPartner(partner));
    }


    @Operation(summary = "점장 정보 수정", description = "파트너 ID를 기준으로 이름, 전화번호, 비밀번호를 변경합니다.")
    @PutMapping("/{partnerId}")
    public ResponseEntity<Partner> updatePartner(@PathVariable Long partnerId,
                                                 @RequestBody PartnerDto partnerDto) {
        Partner updatedPartner = partnerService.updatePartner(partnerId, partnerDto);
        return ResponseEntity.ok(updatedPartner);
    }

    @Operation(summary = "점장 로그인", description = "이메일과 비밀번호를 통해 로그인을 진행합니다.")
    @PostMapping("/login")
    public ResponseEntity<Partner> loginPartner(@RequestBody PartnerRegisterRequest request) {
        Partner partner = partnerService.login(request);
        return ResponseEntity.ok(partner);
    }

    @Operation(summary = "점장 파트너쉽 탈퇴", description = "파트너가 본인의 계정을 삭제합니다.")
    @DeleteMapping("/{partnerId}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long partnerId){
        partnerService.deletePartner(partnerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "점장 조회", description = "파트너 ID를 통해 정보를 조회합니다.")
    @GetMapping("/{partnerId}")
    public ResponseEntity<PartnerDto> getPartner(@PathVariable Long partnerId){
        PartnerDto partnerDto = partnerService.getPartnerById(partnerId);
        return ResponseEntity.ok(partnerDto);
    }

}
