package com.shofiqul.socket.controllers;

import com.shofiqul.socket.dto.ApiResponse;
import com.shofiqul.socket.records.SignupRequest;
import com.shofiqul.socket.records.SignupResponse;
import com.shofiqul.socket.servicesImpl.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authService;

    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/login")
    public void login() {

    }
}
