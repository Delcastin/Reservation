package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Partner;
import com.zerobase.reservation.dto.partner.PartnerDto;
import com.zerobase.reservation.dto.partner.PartnerRegisterRequest;

import java.util.Optional;

public interface PartnerService {

    Partner registerPartner(Partner partner);

    Optional<Partner> findByEmail(String email);

    Partner findById(Long id);

    String encodePassword(String password);

    Partner login(PartnerRegisterRequest request);

    void deletePartner(Long partnerId);

    Partner updatePartner(Long partnerId, PartnerDto partnerDto);

    PartnerDto getPartnerById(Long partnerId);
}
