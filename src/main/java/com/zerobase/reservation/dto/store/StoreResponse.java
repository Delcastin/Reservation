package com.zerobase.reservation.dto.store;


import com.zerobase.reservation.domain.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class StoreResponse {

    private Long id;
    private String name;
    private String description;
    private String categoryName;
    private Long partnerId;

    public static StoreResponse fromStore(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .categoryName(store.getStoreCategory() != null ? store.getStoreCategory().getName() : null)
                .partnerId(store.getPartner() != null ? store.getPartner().getId() : null)
                .build();
    }
}
