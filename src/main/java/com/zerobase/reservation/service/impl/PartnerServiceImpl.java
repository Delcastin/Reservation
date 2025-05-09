package com.zerobase.reservation.service.impl;

import com.zerobase.reservation.domain.Partner;
import com.zerobase.reservation.repository.PartnerRepository;
import com.zerobase.reservation.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;

    @Override
    public Partner registerPartner(Partner partner) {
        return partnerRepository.save(partner);
    }

    @Override
    public Optional<Partner> findByEmail(String email) {
        return partnerRepository.findByEmail(email);
    }

    @Override
    public Partner findById(Long id) {
        return partnerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 파트너를 찾을 수 없습니다."));
    }
}
