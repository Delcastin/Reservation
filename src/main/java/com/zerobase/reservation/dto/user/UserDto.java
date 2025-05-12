package com.zerobase.reservation.dto.user;

import com.zerobase.reservation.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private String phone;
    private String password;

    public static UserDto from(UserRegisterRequest request) {
        UserDto dto = new UserDto();
        dto.setName(request.getName());
        dto.setEmail(request.getEmail());
        return dto;
    }
}
