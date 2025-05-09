package com.zerobase.reservation.dto.store;


import com.zerobase.reservation.domain.Store;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRegisterRequest {

    private String name;
    private String description;
    private Long categoryId;
    private Long partnerId;

    public Store toEntity(){
        return Store.builder()
                .name(this.name)
                .description(this.description)
                .build();
    }
}
