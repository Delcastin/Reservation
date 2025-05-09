package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Partner;

import java.util.Optional;

public interface PartnerService {

    Partner registerPartner(Partner partner);

    Optional<Partner> findByEmail(String email);

    Partner findById(Long id);
}
