package com.yun.springbootmall.controller;

import com.yun.springbootmall.dto.UserRequest;
import com.yun.springbootmall.model.User;
import com.yun.springbootmall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRequest userRequest) {
        Integer userId = userService.register(userRequest);
        User resUser = userService.findUserById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(resUser);
    }

    @PostMapping("users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserRequest userRequest) {
        User resUser = userService.login(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(resUser);
    }
}
