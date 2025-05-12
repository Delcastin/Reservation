package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByStoreCategory_Id(Long id);
    List<Store> findByPartner_Id(Long partnerId);

    Collection<Object> findAllByOrderByNameAsc();

    List<Store> findByPartner_NameContainingIgnoreCaseOrderByNameAsc(String partnerName);

    List<Store> findByStoreCategory_Name(String categoryName);
}
