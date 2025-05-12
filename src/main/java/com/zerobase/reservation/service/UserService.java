package com.zerobase.reservation.service;


import com.zerobase.reservation.domain.User;
import com.zerobase.reservation.dto.user.UserDto;
import com.zerobase.reservation.dto.user.UserRegisterRequest;

import java.util.Optional;

public interface UserService {

    // 회원 가입
    User registerUser(User user);

    // 이메일로 회원 조회
    Optional<User> findByEmail(String email);

    // 아이디로 회원 조회
    User findById(Long id);

    // 로그인 처리
    User login(UserRegisterRequest request);

    // 회원 삭제
    void deleteUser(Long userId);

    // 회원 정보 수정
    User updateUser(Long userId, UserDto userDto);
}
