package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findByEmail(String email);

    boolean existsByEmail(String email);
}
