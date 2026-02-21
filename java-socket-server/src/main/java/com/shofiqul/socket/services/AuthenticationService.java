package com.shofiqul.socket.services;


import com.shofiqul.socket.dto.ApiResponse;
import com.shofiqul.socket.records.LoginRequest;
import com.shofiqul.socket.records.LoginResponse;
import com.shofiqul.socket.records.SignupRequest;
import com.shofiqul.socket.records.SignupResponse;
import jakarta.validation.Valid;

public interface AuthenticationService {
    ApiResponse<SignupResponse> signup(SignupRequest signupRequest);

    ApiResponse<LoginResponse> login(@Valid LoginRequest loginRequest);
}
