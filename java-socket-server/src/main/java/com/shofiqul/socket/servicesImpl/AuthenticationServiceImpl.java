package com.shofiqul.socket.servicesImpl;

import com.shofiqul.socket.dto.ApiResponse;
import com.shofiqul.socket.entity.GroupsEntity;
import com.shofiqul.socket.entity.UserEntity;
import com.shofiqul.socket.entity.UserGroupEntity;
import com.shofiqul.socket.records.LoginRequest;
import com.shofiqul.socket.records.LoginResponse;
import com.shofiqul.socket.records.SignupRequest;
import com.shofiqul.socket.records.SignupResponse;
import com.shofiqul.socket.repositories.AuthenticationRepo;
import com.shofiqul.socket.repositories.UserGroupRepo;
import com.shofiqul.socket.services.AuthenticationService;
import com.shofiqul.socket.utility.JwtUtils;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationRepo authRepo;
    private final GroupsServiceImpl groupsServiceimpl;
    private final UserGroupRepo userGroupRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @Override
    public ApiResponse<SignupResponse> signup(SignupRequest signupRequest) {
        try {
            Optional<UserEntity> userExist = authRepo.findByUsernameOrEmail(signupRequest.username(), signupRequest.email());
            if (userExist.isPresent()) {
                return ApiResponse.ok(null, "User with given Username or Email already exists!");
            }

            UserEntity userEntity = new UserEntity(
                    signupRequest.username(),
                    signupRequest.fullName(),
                    signupRequest.email(),
                    passwordEncoder.encode(signupRequest.password()),
                    null);

            UserEntity savedUser = authRepo.save(userEntity);
            GroupsEntity broadcastGroup = groupsServiceimpl.getBroadcastGroupEntity();

            UserGroupEntity userGroupEntity = new UserGroupEntity();
            userGroupEntity.setGroup(broadcastGroup);
            userGroupEntity.setUser(savedUser);
            userGroupEntity.setJoinedAt(new Timestamp(System.currentTimeMillis()));
            userGroupRepo.save(userGroupEntity);

            SignupResponse response = new SignupResponse(savedUser.getUsername(), savedUser.getFullName(), savedUser.getEmail());

            return ApiResponse.ok(response, "Signup successful!");

        } catch (Exception e) {
            return getApiResponseForConstraintError(e);
        }
    }

    @Override
    public ApiResponse<LoginResponse> login(LoginRequest loginRequest) {
        try {
            String userHandle = loginRequest.userHandle();
            Optional<UserEntity> existingUser = authRepo.findByUserHandle(userHandle);

            if (existingUser.isEmpty()) {
                return ApiResponse.error(null, "Invalid username/email");
            }

            UserEntity user = existingUser.get();

            if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
                return ApiResponse.error(null, "Invalid password");
            }

            String token = getJwtWithClaims(user, loginRequest);
            LoginResponse response = new LoginResponse(user.getId(), user.getUsername(), token);

            return ApiResponse.ok(response, "Login successful!");
        } catch (BadCredentialsException e) {
            return ApiResponse.error(null, "Invalid credentials");
        } catch (Exception e) {
            return getApiResponseForConstraintError(e);
        }
    }

    private String getJwtWithClaims(UserEntity userEntity, LoginRequest loginRequest) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(userEntity.getUsername(), loginRequest.password()));

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userEntity.getId());
        claims.put("username", userEntity.getUsername());

        return jwtUtils.generateToken(claims, userEntity.getUsername());
    }

    private <T> ApiResponse<T> getApiResponseForConstraintError(Exception e) {
        Throwable cause = e;

        // Unwrap until I find the ConstraintViolationException
        while (cause != null && !(cause instanceof ConstraintViolationException)) {
            cause = cause.getCause();
        }

        if (cause instanceof ConstraintViolationException cve) {
            List<String> violations = new ArrayList<>();
            cve.getConstraintViolations().forEach(violation -> {
                log.error("Validation failed — field: [{}], message: [{}]", violation.getPropertyPath(), violation.getMessage());
                violations.add(violation.getMessage());
            });
            return ApiResponse.error(null, "Validation failed: " + String.join(", ", violations));
        }

        log.error("Something went wrong while Logging in/Signing up the user: {}", e.getMessage(), e);
        return ApiResponse.error(null, "Something went wrong while Logging in/Signing up the user");
    }

}
