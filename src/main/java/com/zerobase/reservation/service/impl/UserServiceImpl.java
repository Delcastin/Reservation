package com.zerobase.reservation.service.impl;

import com.zerobase.reservation.domain.User;
import com.zerobase.reservation.dto.user.UserDto;
import com.zerobase.reservation.dto.user.UserRegisterRequest;
import com.zerobase.reservation.exception.AlreadyExistsException;
import com.zerobase.reservation.exception.CustomException;
import com.zerobase.reservation.exception.ErrorCode;
import com.zerobase.reservation.exception.NotFoundException;
import com.zerobase.reservation.repository.UserRepository;
import com.zerobase.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException("이 Email은 이미 사용중입니다. Email명 : " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자의 ID가 없습니다. ID명 : " + id));
    }

    @Override
    public User login(UserRegisterRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return user;
    }


    @Override
    public void deleteUser(Long userId) {

        User user = userRepository.findByEmail(String.valueOf(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);

    }

    @Override
    public User updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setName(userDto.getName());
        user.setPhone(userDto.getPhone());
        if(userDto.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return userRepository.save(user);
    }
}
