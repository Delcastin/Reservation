package com.zerobase.reservation.dto.store;


import com.zerobase.reservation.domain.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class StoreRegisterRequest {

    private String name;
    private String description;
    private Long categoryId;
    private Long partnerId;
    private String address;
    private String phone;

    public Store toEntity(){
        return Store.builder()
                .name(this.name)
                .description(this.description)
                .build();
    }

    @Builder
    public StoreRegisterRequest(String name, String description, Long categoryId, Long partnerId, String address, String phone) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.partnerId = partnerId;
        this.address = address;
        this.phone = phone;
    }
}
