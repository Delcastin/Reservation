package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.store.StoreCategory;

import java.util.List;
import java.util.Optional;

public interface StoreService {

    Store registerStore(Store store);

    List<Store> getAllStores();

    List<Store> getStoresByCategory(Long categoryId);

    List<Store> getStoresByPartner(Long partnerId);

    Optional<StoreCategory> findByName(String name);

    Store updateStore(Long storeId, Store updateStore);

    void deleteStore(Long storeId);
}
