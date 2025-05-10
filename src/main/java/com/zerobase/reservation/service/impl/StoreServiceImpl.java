package com.zerobase.reservation.service.impl;

import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.repository.StoreCategoryRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.service.StoreService;
import com.zerobase.reservation.store.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;

    @Override
    public Store registerStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public List<Store> getStoresByCategory(Long categoryId) {
        return storeRepository.findByStoreCategory_Id(categoryId);
    }

    @Override
    public List<Store> getStoresByPartner(Long partnerId) {
        return storeRepository.findByPartner_Id(partnerId);
    }

    @Override
    public Optional<StoreCategory> findByName(String name) {
        return storeCategoryRepository.findByName(name);
    }
}
