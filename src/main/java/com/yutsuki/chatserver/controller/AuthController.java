package com.yutsuki.chatserver.controller;

import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.*;
import com.yutsuki.chatserver.model.response.TokenResponse;
import com.yutsuki.chatserver.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<TokenResponse> login(@Valid @RequestBody LoginRequest request) throws BaseException {
        return authService.login(request);
    }

    @PostMapping("/signup")
    public Result<Void> signup(@Valid @RequestBody SignupRequest request) throws BaseException {
        return authService.signup(request);
    }

    @PostMapping("/refresh")
    public Result<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) throws BaseException {
        return authService.refresh(request);
    }

    @PostMapping("/verify")
    public Result<TokenResponse> verifyEmailCode(@RequestBody VerifyEmailCodeRequest request) throws BaseException {
        return authService.verifyEmailCode(request);
    }

    @PostMapping("/resend-verify")
    public Result<Void> resendVerifyEmailCode(@RequestBody ResendVerifyEmailCodeRequest request) throws BaseException {
        return authService.resendVerifyEmailCode(request);
    }

    @PostMapping("/forgot-password")
    public Result<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) throws BaseException {
        return authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@RequestBody ResetPasswordRequest request) throws BaseException {
        return authService.resetPassword(request);
    }

}
