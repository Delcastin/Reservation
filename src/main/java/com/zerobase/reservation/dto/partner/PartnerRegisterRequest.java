package com.zerobase.reservation.dto.partner;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerRegisterRequest {

    private String email;
    private String password;
    private String name;
    private String phone;
}
