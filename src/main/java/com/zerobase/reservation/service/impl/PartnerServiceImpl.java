package com.zerobase.reservation.service.impl;

import com.zerobase.reservation.domain.Partner;
import com.zerobase.reservation.dto.partner.PartnerDto;
import com.zerobase.reservation.dto.partner.PartnerRegisterRequest;
import com.zerobase.reservation.exception.AlreadyExistsException;
import com.zerobase.reservation.exception.CustomException;
import com.zerobase.reservation.exception.ErrorCode;
import com.zerobase.reservation.repository.PartnerRepository;
import com.zerobase.reservation.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Partner registerPartner(Partner partner) {

        if(partnerRepository.existsByEmail(partner.getEmail())){
            throw new AlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        partner.setPassword(passwordEncoder.encode(partner.getPassword()));

        return partnerRepository.save(partner);
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Partner login(PartnerRegisterRequest request) {
        Partner partner = partnerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), partner.getPassword())){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return partner;
    }

    @Override
    public void deletePartner(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        partnerRepository.delete(partner);
    }

    @Override
    public Partner updatePartner(Long partnerId, PartnerDto partnerDto) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        partner.setName(partnerDto.getName());
        partner.setPhone(partnerDto.getPhone());
        if(partnerDto.getPassword() != null){
            partner.setPassword(passwordEncoder.encode(partner.getPassword()));
        }

        return partnerRepository.save(partner);
    }

    @Override
    public PartnerDto getPartnerById(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return PartnerDto.fromEntity(partner);
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
