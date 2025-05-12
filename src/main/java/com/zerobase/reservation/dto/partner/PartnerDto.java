package com.zerobase.reservation.dto.partner;


import com.zerobase.reservation.domain.Partner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerDto {

    private Long id;
    private String email;
    private String name;
    private String phone;

    private String password;

    public static PartnerDto fromEntity(Partner partner) {
        PartnerDto partnerDto = new PartnerDto();
        partnerDto.setId(partner.getId());
        partnerDto.setEmail(partner.getEmail());
        partnerDto.setName(partner.getName());
        partnerDto.setPhone(partner.getPhone());
        return partnerDto;

    }
}
