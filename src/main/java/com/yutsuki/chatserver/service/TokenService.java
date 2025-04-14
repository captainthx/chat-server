package com.yutsuki.chatserver.service;

import com.yutsuki.chatserver.entity.User;
import com.yutsuki.chatserver.enums.TokenType;
import com.yutsuki.chatserver.exception.AuthException;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.repository.UserRepository;
import com.yutsuki.chatserver.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final UserRepository userRepository;

    @Value("${api.jwt.issuer}")
    private String issuer;

    @Value("${api.jwt.expiration.access-token}")
    private Duration accessExpiration;

    @Value("${api.jwt.expiration.refresh-token}")
    private Duration refreshExpiration;

    public Jwt generateAccessToken(final Long userId) {
        Instant now = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(accessExpiration))
                .id(RandomUtils.notSymbol())
                .subject(String.valueOf(userId))
                .claim("type", TokenType.ACCESS.name())
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
    }

    public Jwt generateRefreshToken(final Long userId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(refreshExpiration))
                .id(RandomUtils.notSymbol())
                .subject(String.valueOf(userId))
                .claim("type", TokenType.REFRESH.name())
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims));
    }

    public Jwt decode(final String token) throws BaseException {
        try {
            return this.decoder.decode(token);
        } catch (Exception e) {
            throw AuthException.unauthorized();
        }
    }

    public boolean isAccessToken(final Jwt jwt) {
        return Objects.equals(jwt.getClaim("type"), TokenType.ACCESS.name());
    }

    public boolean isRefreshToken(final Jwt jwt) {
        return Objects.equals(jwt.getClaim("type"), TokenType.REFRESH.name());
    }

    public User getUserByToken(final String token) throws BaseException {
        final Jwt jwt = this.decode(token);
        return userRepository.findById(Long.valueOf(jwt.getSubject()))
                .orElseThrow(AuthException::unauthorized);
    }
}
