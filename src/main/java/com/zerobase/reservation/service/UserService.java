package com.zerobase.reservation.service;


import com.zerobase.reservation.domain.User;

import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    Optional<User> findByEmail(String email);

    User findById(Long id);
}
