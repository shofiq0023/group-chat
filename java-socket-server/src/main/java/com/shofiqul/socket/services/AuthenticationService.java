package com.shofiqul.socket.services;


import com.shofiqul.socket.dto.ApiResponse;
import com.shofiqul.socket.records.SignupRequest;
import com.shofiqul.socket.records.SignupResponse;

public interface AuthenticationService {
    ApiResponse<SignupResponse> signup(SignupRequest signupRequest);
}
