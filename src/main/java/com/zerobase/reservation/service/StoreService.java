package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.store.StoreRegisterRequest;
import com.zerobase.reservation.dto.store.StoreResponse;
import com.zerobase.reservation.store.StoreCategory;

import java.util.List;
import java.util.Optional;

public interface StoreService {

    StoreResponse registerStore(StoreRegisterRequest request);

    List<Store> getAllStores();

    List<Store> getStoresByCategory(Long categoryId);

    List<Store> getStoresByPartner(Long partnerId);

    Optional<StoreCategory> findByName(String name);

    StoreResponse updateStore(Long storeId, StoreRegisterRequest request, Long partnerId);

    void deleteStore(Long storeId, Long partnerId);

    List<StoreResponse> getAllStoresOrderByName();

    List<StoreResponse> searchStoresByPartnerName(String partnerName);

    StoreResponse getStoreById(Long storeId);

    List<StoreResponse> getStoresByCategory(String categoryName);
}
