package com.zerobase.reservation.dto.store;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {

    private Long id;
    private String name;
    private String address;
    private String description;
    private String category;
    private List<String> imageUrls;
}
