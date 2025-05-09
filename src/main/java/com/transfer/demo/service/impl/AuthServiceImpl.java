package com.transfer.demo.service.impl;

import com.transfer.demo.config.security.JwtService;
import com.transfer.demo.dto.TokenResponseDto;
import com.transfer.demo.ex.UserNotFoundException;
import com.transfer.demo.ex.UserUnauthorizedException;
import com.transfer.demo.model.User;
import com.transfer.demo.repository.UserRepository;
import com.transfer.demo.request.EmailLoginRequest;
import com.transfer.demo.request.PhoneLoginRequest;
import com.transfer.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String NOT_FOUND_BY_EMAIL_MESSAGE = "No such user found. by email: ";
    private static final String NOT_FOUND_BY_PHONE_MESSAGE = "No such user found. by phone: ";


    public TokenResponseDto loginByEmail(EmailLoginRequest request) {

        log.info("Auth service started login by Email with request: {}", request);
        User user = userRepository.findByEmails_email(request.email())
                .orElseThrow(() -> new UserNotFoundException(NOT_FOUND_BY_EMAIL_MESSAGE + request.email()));
        String encodedPassword = passwordEncoder.encode(request.password());
        log.info(encodedPassword);
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UserUnauthorizedException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new TokenResponseDto(token);
    }

    public TokenResponseDto loginByPhone(PhoneLoginRequest request) {

        User user = userRepository.findByPhones_phone(Long.parseLong(request.phone()))
                .orElseThrow(() -> new UserNotFoundException(NOT_FOUND_BY_PHONE_MESSAGE + request.phone()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UserUnauthorizedException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new TokenResponseDto(token);
    }
}