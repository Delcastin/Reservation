package com.zerobase.reservation.controller;


import com.zerobase.reservation.dto.store.StoreRegisterRequest;
import com.zerobase.reservation.dto.store.StoreResponse;
import com.zerobase.reservation.service.PartnerService;
import com.zerobase.reservation.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
@Tag(name = "Store API", description = "매장 관련 기능")
public class StoreController {

    private final StoreService storeService;

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


    @Operation(summary = "매장 등록", description = "새로운 매장을 등록합니다.")
    @PostMapping
    public ResponseEntity<StoreResponse> registerStore(@RequestBody StoreRegisterRequest request) {

        StoreResponse response = storeService.registerStore(request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "매장 정보 수정", description = "매장의 상세 정보를 변경합니다.")
    @PatchMapping("/{storeId}") // 매장 정보 수정 기능
    public ResponseEntity<StoreResponse> updateStore(@PathVariable Long storeId,
                                                     @RequestBody StoreRegisterRequest request,
                                                     @RequestParam Long partnerId) {
        return ResponseEntity.ok(storeService.updateStore(storeId, request, partnerId));
    }

    @Operation(summary = "매장 삭제 기능", description = "등록되어 있는 매장을 매장 ID와 점장의 ID로 삭제합니다.")
    @DeleteMapping("/stores/{id}") // 매장 정보 삭제 기능
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId,
                                            @RequestParam Long partnerId) {
        storeService.deleteStore(storeId, partnerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "매장 목록 조회 - 가나다 순", description = "전체 매장 목록을 가나다 순으로 정렬하여 확인합니다.")
    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStoresOrderByName(){
        return ResponseEntity.ok(storeService.getAllStoresOrderByName());
    }

    @Operation(summary = "매장 검색 기능 - 점장의 이름으로 조회하는 기능", description = "전체 매장 목록 중 점장의 이름으로 검색하여 필터링된 결과를 출력합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<StoreResponse>> searchStoresByPartnerName(@RequestParam String partnerName){
        return ResponseEntity.ok(storeService.searchStoresByPartnerName(partnerName));
    }

    @Operation(summary = "매장 상세 정보 조회 기능", description = "매장을 선택하면 그 매장의 상세 정보를 받아옵니다.")
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponse> getStoreById(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStoreById(storeId));
    }

    @Operation(summary = "매장 검색 기능 - 카테고리별로 조회하는 기능", description = "매장의 종류로 검색하여 매장들을 출력하는 기능입니다.")
    @GetMapping("/category")
    public ResponseEntity<List<StoreResponse>> getStoresByCategory(@RequestParam String categoryName) {
        return ResponseEntity.ok(storeService.getStoresByCategory(categoryName));
    }


}
