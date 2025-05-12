package com.zerobase.reservation.service;


import com.zerobase.reservation.domain.User;
import com.zerobase.reservation.exception.NotFoundException;
import com.zerobase.reservation.repository.UserRepository;
import com.zerobase.reservation.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_success(){
        User user = new User();
        user.setEmail("test@user.com");
        user.setPassword("password");
        user.setName("test");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.registerUser(user);

        assertEquals("test@user.com", result.getEmail());
        assertEquals("test", result.getName());
    }

    @Test
    void findUserById_success(){
        User user = new User();
        user.setId(1L);
        user.setEmail("test@user.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("test@user.com", result.getEmail());

    }

    @Test
    void findById_notFound_throwsException(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(1L));
    }
}
