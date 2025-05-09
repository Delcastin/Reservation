package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByCategory_Id(Long categoryId);
    List<Store> findByPartner_Id(Long partnerId);
}
