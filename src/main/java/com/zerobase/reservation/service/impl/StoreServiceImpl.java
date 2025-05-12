package com.zerobase.reservation.service.impl;

import com.zerobase.reservation.domain.Partner;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.store.StoreRegisterRequest;
import com.zerobase.reservation.dto.store.StoreResponse;
import com.zerobase.reservation.exception.CustomException;
import com.zerobase.reservation.exception.ErrorCode;
import com.zerobase.reservation.repository.PartnerRepository;
import com.zerobase.reservation.repository.StoreCategoryRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.service.StoreService;
import com.zerobase.reservation.store.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;
    private final PartnerRepository partnerRepository;

    @Override
    public StoreResponse registerStore(StoreRegisterRequest request) {

        Partner partner = partnerRepository.findById(request.getPartnerId())
                .orElseThrow(() -> new CustomException(ErrorCode.PARTNER_NOT_FOUND));



        Store store = storeRepository.save(Store.builder()
                        .name(request.getName())
                        .address(request.getAddress())
                        .phone(request.getPhone())
                        .partner(partner)
                        .description(request.getDescription())
                        .build());
        return StoreResponse.fromStore(store);
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

    @Override
    public StoreResponse updateStore(Long storeId, StoreRegisterRequest request, Long partnerId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!store.getPartner().getId().equals(partnerId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        store.setName(request.getName());
        store.setAddress(request.getAddress());
        store.setPhone(request.getPhone());
        store.setDescription(request.getDescription());


        return StoreResponse.fromStore(storeRepository.save(store));
    }

    @Override
    public void deleteStore(Long storeId, Long partnerId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if(!store.getPartner().getId().equals(partnerId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        storeRepository.delete(store);
    }

    @Override
    public List<StoreResponse> getAllStoresOrderByName() {
        List<StoreResponse> collect = storeRepository.findAllByOrderByNameAsc().stream()
                .map((Object store) -> StoreResponse.fromStore((Store) store))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<StoreResponse> searchStoresByPartnerName(String partnerName) {
        return storeRepository.findByPartner_NameContainingIgnoreCaseOrderByNameAsc(partnerName).stream()
                .map(StoreResponse::fromStore)
                .collect(Collectors.toList());
    }

    @Override
    public StoreResponse getStoreById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        return StoreResponse.fromStore(store);
    }

    @Override
    public List<StoreResponse> getStoresByCategory(String categoryName) {

        List<Store> stores = storeRepository.findByStoreCategory_Name(categoryName);

        return stores.stream()
                .map(StoreResponse::fromStore)
                .toList();
    }

}
