package com.yutsuki.chatserver.service;

import com.yutsuki.chatserver.entity.User;
import com.yutsuki.chatserver.exception.AuthException;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long userId) throws BaseException {
        return getUserByIdThrow(userId, AuthException.userNotFound());
    }

    public User getUserByIdThrow(Long userId, BaseException e) throws BaseException {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("GetUserByIdThrow-[block]:(not found). userId: {}", userId);
                    return e;
                });
    }

    public  User getUserByUsername(String username) throws BaseException{
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.warn("GetUserByUsername-[block]:(not found). username: {}", username);
            return AuthException.userNotFound();
        });
    }
}
