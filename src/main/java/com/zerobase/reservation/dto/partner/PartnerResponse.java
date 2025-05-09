package com.zerobase.reservation.dto.partner;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class PartnerResponse {

    private Long id;
    private String email;
    private String name;
}
