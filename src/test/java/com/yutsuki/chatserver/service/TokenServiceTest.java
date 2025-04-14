package com.yutsuki.chatserver.service;

import com.yutsuki.chatserver.entity.User;
import com.yutsuki.chatserver.enums.TokenType;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TokenServiceTest {

    private TokenService tokenService;

    @Mock
    private JwtEncoder encoder;

    @Mock
    private JwtDecoder decoder;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(
                encoder,
                decoder,
                userRepository
        );

        ReflectionTestUtils.setField(tokenService, "issuer", "issuer");
        ReflectionTestUtils.setField(tokenService, "accessExpiration", Duration.ofHours(1));
        ReflectionTestUtils.setField(tokenService, "refreshExpiration", Duration.ofHours(2));
    }

    @Test
    @Order(1)
    void generateAccessToken() {
        JwtClaimsSet accessTokenClaims = JwtClaimsSet.builder()
                .issuer("issuer")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofHours(1)))
                .id("randomId")
                .subject("999")
                .claim("type", TokenType.ACCESS.name())
                .build();
        Jwt jwt = Jwt.withTokenValue("tokenValue").header("alg", "none").claims(claims -> claims.putAll(accessTokenClaims.getClaims())).build();
        when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        Jwt token = tokenService.generateAccessToken(999L);
        assertNotNull(token);
    }

    @Test
    @Order(2)
    void generateRefreshToken() {
        JwtClaimsSet refreshTokenClaims = JwtClaimsSet.builder()
                .issuer("issuer")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofHours(2)))
                .id("randomId")
                .subject("999")
                .claim("type", TokenType.REFRESH.name())
                .build();
        Jwt jwt = Jwt.withTokenValue("tokenValue").header("alg", "none").claims(claims -> claims.putAll(refreshTokenClaims.getClaims())).build();
        when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        Jwt token = tokenService.generateRefreshToken(999L);
        assertNotNull(token);
    }

    @Test
    @Order(3)
    void decode() throws BaseException {
        JwtClaimsSet refreshTokenClaims = JwtClaimsSet.builder()
                .issuer("issuer")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofHours(2)))
                .id("randomId")
                .subject("999")
                .claim("type", TokenType.REFRESH.name())
                .build();
        Jwt jwt = Jwt.withTokenValue("tokenValue").header("alg", "none").claims(claims -> claims.putAll(refreshTokenClaims.getClaims())).build();
        when(decoder.decode("tokenValue")).thenReturn(jwt);

        Jwt decodedJwt = tokenService.decode("tokenValue");
        assertNotNull(decodedJwt);
    }

    @Test
    @Order(4)
    void isAccessToken() {
        JwtClaimsSet accessTokenClaims = JwtClaimsSet.builder()
                .issuer("issuer")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofHours(1)))
                .id("randomId")
                .subject("999")
                .claim("type", TokenType.ACCESS.name())
                .build();
        Jwt jwt = Jwt.withTokenValue("tokenValue").header("alg", "none").claims(claims -> claims.putAll(accessTokenClaims.getClaims())).build();

        boolean isAccessToken = tokenService.isAccessToken(jwt);
        assertTrue(isAccessToken);
    }

    @Test
    @Order(5)
    void isRefreshToken() {
        JwtClaimsSet refreshTokenClaims = JwtClaimsSet.builder()
                .issuer("issuer")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofHours(2)))
                .id("randomId")
                .subject("999")
                .claim("type", TokenType.REFRESH.name())
                .build();
        Jwt jwt = Jwt.withTokenValue("tokenValue").header("alg", "none").claims(claims -> claims.putAll(refreshTokenClaims.getClaims())).build();

        boolean isRefreshToken = tokenService.isRefreshToken(jwt);
        assertTrue(isRefreshToken);
    }

    @Test
    @Order(6)
    void getUserByToken() throws BaseException {
        JwtClaimsSet refreshTokenClaims = JwtClaimsSet.builder()
                .issuer("issuer")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofHours(2)))
                .id("randomId")
                .subject("999")
                .claim("type", TokenType.REFRESH.name())
                .build();
        Jwt jwt = Jwt.withTokenValue("tokenValue").header("alg", "none").claims(claims -> claims.putAll(refreshTokenClaims.getClaims())).build();
        when(decoder.decode("tokenValue")).thenReturn(jwt);

        when(userRepository.findById(999L)).thenReturn(Optional.of(new User()));

        User user = tokenService.getUserByToken("tokenValue");
        assertNotNull(user);
    }
}