package com.zerobase.reservation.repository;

import com.zerobase.reservation.store.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {

    Optional<StoreCategory> findByName(String name);

}
