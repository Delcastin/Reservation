package com.zerobase.reservation.dto.partner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerRegisterRequest {

    private String email;
    private String password;
    private String name;
}
