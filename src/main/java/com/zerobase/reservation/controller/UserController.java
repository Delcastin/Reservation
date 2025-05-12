package com.zerobase.reservation.controller;


import com.zerobase.reservation.domain.User;
import com.zerobase.reservation.dto.user.UserDto;
import com.zerobase.reservation.dto.user.UserRegisterRequest;
import com.zerobase.reservation.dto.user.UserResponse;
import com.zerobase.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}") // 사용자 정보 조회
    public ResponseEntity<User> getUser(@PathVariable Long id) {

        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register") // 사용자 회원가입
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegisterRequest request) {

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .build();

        User savedUser = userService.registerUser(user);

        UserResponse response = UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login") // 로그인
    public ResponseEntity<User> login(@RequestBody UserRegisterRequest request) {

        return ResponseEntity.ok(userService.login(request));
    }

    @PutMapping("/{userId}") // 사용자 정보 수정
    public ResponseEntity<User> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody UserRegisterRequest request
    ){
        UserDto dto = UserDto.from(request);
        return ResponseEntity.ok(userService.updateUser(userId, dto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


}
