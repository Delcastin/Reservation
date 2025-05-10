package com.zerobase.reservation.controller;


import com.zerobase.reservation.domain.Partner;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.store.StoreRegisterRequest;
import com.zerobase.reservation.dto.store.StoreResponse;
import com.zerobase.reservation.service.PartnerService;
import com.zerobase.reservation.service.StoreService;
import com.zerobase.reservation.store.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;
    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        return ResponseEntity.ok(
                storeService.getAllStores().stream()
                        .map(StoreResponse::fromStore)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<StoreResponse>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(
                storeService.getStoresByCategory(categoryId).stream()
                        .map(StoreResponse::fromStore)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/partner/{partnerId}")
    public ResponseEntity<List<StoreResponse>> getByPartner(@PathVariable Long partnerId) {
        return ResponseEntity.ok(
                storeService.getStoresByPartner(partnerId).stream()
                        .map(StoreResponse::fromStore)
                        .collect(Collectors.toList()));
    }

    @PostMapping("/register")
    public ResponseEntity<StoreResponse> registerStore(@RequestBody StoreRegisterRequest request) {
        // 파트너 조회
        Partner partner = partnerService.findById(request.getPartnerId());
        // 카테고리 조회
        Optional<StoreCategory> categoryOpt = storeService.findByName(request.getCategoryId().toString());

        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // DTO → Entity 변환
        Store store = request.toEntity();
        store.setPartner(partner);
        store.setStoreCategory(categoryOpt.get());

        Store savedStore = storeService.registerStore(store);
        return ResponseEntity.ok(StoreResponse.fromStore(savedStore));
    }


}
