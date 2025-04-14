package com.yutsuki.chatserver.service;


import com.yutsuki.chatserver.entity.User;
import com.yutsuki.chatserver.enums.UserRole;
import com.yutsuki.chatserver.exception.AuthException;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.exception.UserException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.*;
import com.yutsuki.chatserver.model.response.AccountResponse;
import com.yutsuki.chatserver.model.response.TokenResponse;
import com.yutsuki.chatserver.repository.UserRepository;
import com.yutsuki.chatserver.utils.RandomUtils;
import com.yutsuki.chatserver.utils.RedisKeyUtils;
import com.yutsuki.chatserver.utils.ResultUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final RedisService redisService;

    public Result<TokenResponse> login(LoginRequest request) throws BaseException {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            log.warn("Login-[block]:(not found account). request:{}", request);
            throw AuthException.credentialsInvalid();
        }

        User user = userOptional.get();

        if (passwordNotMatch(request.getPassword(), user.getPassword())) {
            log.warn("Login-[block]:(password not match). request:{}", request);
            throw AuthException.credentialsInvalid();
        }


        TokenResponse response = createTokenResponse(user);

        return ResultUtils.successWithMessage(response, "Login successfully.");
    }

    public Result<Void> signup(SignupRequest request) throws BaseException {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Signup-[block]:(email duplicate). request:{}", request);
            throw UserException.emailDuplicate();
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setVerified(Boolean.FALSE);

        userRepository.save(user);

//        String code = RandomUtils.Number6Characters();
//        String redisKey = RedisKeyUtils.emailVerification(user.getEmail());
//        redisService.set(redisKey, code);

//        mailService.sendEmailVerificationCode(user.getEmail(), code);

        return ResultUtils.successWithMessage("Sign Up successfully.");
    }

    public Result<TokenResponse> refresh(RefreshTokenRequest request) throws BaseException {
        Jwt jwt = tokenService.decode(request.getRefreshToken());

        if (!tokenService.isRefreshToken(jwt)) {
            log.warn("Refresh-[block]:(invalid refresh token). request:{}", request);
            throw AuthException.unauthorized();
        }

        Optional<User> userOptional = userRepository.findById(Long.parseLong(jwt.getSubject()));

        if (userOptional.isEmpty()) {
            log.warn("Refresh-[block]:(not found user). request:{}", request);
            throw AuthException.unauthorized();
        }

        TokenResponse response = createTokenResponse(userOptional.get());

        return ResultUtils.successWithMessage(response, "Refresh token successfully.");
    }

    public Result<TokenResponse> verifyEmailCode(VerifyEmailCodeRequest request) throws BaseException {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            log.warn("VerifyEmailCode-[block]:(not found user). request:{}", request);
            return AuthException.userNotFound();
        });

        String redisKey = RedisKeyUtils.emailVerification(request.getEmail());
        String verifyCode = redisService.get(redisKey);

        if (!StringUtils.hasText(verifyCode) || !request.getVerifyCode().equals(verifyCode)) {
            log.warn("VerifyEmailCode-[block]:(verifyCode invalid). request:{}", request);
            throw AuthException.verifyCodeInvalid();
        }

        user.setVerified(Boolean.TRUE);
        userRepository.save(user);
        redisService.deleteKey(redisKey);


        TokenResponse response = createTokenResponse(user);

        return ResultUtils.successWithMessage(response, "Email successfully verified.");
    }

    public Result<Void> resendVerifyEmailCode(ResendVerifyEmailCodeRequest request) throws BaseException {

        if (!userRepository.existsByEmail(request.getEmail())) {
            log.warn("ResendVerifyEmailCode-[block]:(not found user). request:{}", request);
            throw AuthException.userNotFound();
        }

        String redisKey = RedisKeyUtils.emailVerification(request.getEmail());
        String code = redisService.get(redisKey);
        if (StringUtils.hasText(code)) {
            redisService.deleteKey(redisKey);
        }

        String newCode = RandomUtils.Number6Characters();
        redisService.set(redisKey, newCode);

        mailService.sendEmailVerificationCode(request.getEmail(), newCode);

        return ResultUtils.successWithMessage("Successfully sent verification code to email.");
    }

    public Result<Void> forgotPassword(ForgotPasswordRequest request) throws BaseException {

        if (!userRepository.existsByEmail(request.getEmail())) {
            log.warn("ForgotPassword-[block]:(not found user). request:{}", request);
            throw AuthException.userNotFound();
        }

        String code = RandomUtils.Number6Characters();
        String redisKey = RedisKeyUtils.forgotPassword(request.getEmail());
        redisService.set(redisKey, code);
        mailService.sendForgotPasswordCode(request.getEmail(), code);

        return ResultUtils.successWithMessage("Successfully sent verification code to email.");
    }

    public Result<Void> resetPassword(ResetPasswordRequest request) throws BaseException {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            log.warn("ResetPassword-[block]:(not found user). request:{}", request);
            return AuthException.userNotFound();
        });

        String redisKey = RedisKeyUtils.forgotPassword(user.getEmail());
        String verifyCode = redisService.get(redisKey);

        if (!StringUtils.hasText(verifyCode) || !request.getVerifyCode().equals(verifyCode)) {
            log.warn("ResetPassword-[block]:(code invalid). request:{}", request);
            throw AuthException.verifyCodeInvalid();
        }

        String newHashPassword = passwordEncoder.encode(request.getPassword());

        user.setPassword(newHashPassword);
        userRepository.save(user);

        return ResultUtils.successWithMessage("Password successfully reset.");
    }

    public User getUser() throws BaseException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("GetUser-[block]:(authentication is null)");
            throw AuthException.unauthorized();
        }

        if (!authentication.isAuthenticated()) {
            log.warn("GetUser-[block]:(not authenticated)");
            throw AuthException.unauthorized();
        }

        if (!(authentication.getCredentials() instanceof Jwt jwt)) {
            log.warn("GetUser-[block]:(credentials is not Jwt)");
            throw AuthException.unauthorized();
        }

        if (!tokenService.isAccessToken(jwt)) {
            log.warn("GetUser-[block]:(not access token)");
            throw AuthException.unauthorized();
        }

        final String userId = authentication.getName();

        if (ObjectUtils.isEmpty(userId)) {
            log.warn("GetUser-[block]:(accountId is empty)");
            throw AuthException.unauthorized();
        }

        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        if (userOptional.isEmpty()) {
            log.warn("GetUser-[block]:(not found user). userId:{}", userId);
            throw AuthException.unauthorized();
        }

        User user = userOptional.get();

        if (!user.getVerified()) {
            log.warn("GetUser-[block]:(email not verified). userId:{}", userId);
            throw AuthException.unauthorized();
        }

        return user;
    }

    public boolean anonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ObjectUtils.isEmpty(authentication) || authentication.getPrincipal().equals("anonymousUser");
    }

    public boolean passwordNotMatch(String raw, String hash) {
        return !passwordEncoder.matches(raw, hash);
    }

    public TokenResponse createTokenResponse(User user) {

        final Jwt accessToken = tokenService.generateAccessToken(user.getId());
        final Jwt refreshToken = tokenService.generateRefreshToken(user.getId());

        TokenResponse response = new TokenResponse();
        response.setAccessToken(accessToken.getTokenValue());
        response.setAccessExpire(Objects.requireNonNull(accessToken.getExpiresAt()).getEpochSecond());
        response.setRefreshToken(refreshToken.getTokenValue());
        response.setRefreshExpire(Objects.requireNonNull(refreshToken.getExpiresAt()).getEpochSecond());

        AccountResponse accountResponse = AccountResponse.fromEntity(user);
        response.setAccount(accountResponse);

        return response;
    }

}
