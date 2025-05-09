package com.zerobase.reservation.dto.partner;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerDto {

    private Long id;
    private String email;
    private String name;
    private String phone;
}
