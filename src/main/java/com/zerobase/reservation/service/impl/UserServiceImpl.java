package com.zerobase.reservation.service.impl;

import com.zerobase.reservation.domain.User;
import com.zerobase.reservation.exception.AlreadyExistsException;
import com.zerobase.reservation.exception.NotFoundException;
import com.zerobase.reservation.repository.UserRepository;
import com.zerobase.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException("이 Email은 이미 사용중입니다. Email명 : " + user.getEmail());
        }
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
}
