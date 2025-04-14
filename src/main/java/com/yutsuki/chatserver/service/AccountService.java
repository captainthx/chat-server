package com.yutsuki.chatserver.service;

import com.yutsuki.chatserver.entity.File;
import com.yutsuki.chatserver.entity.User;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.exception.UserException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.AccountUpdateRequest;
import com.yutsuki.chatserver.model.request.ChangePasswordRequest;
import com.yutsuki.chatserver.model.response.AccountResponse;
import com.yutsuki.chatserver.repository.FileRepository;
import com.yutsuki.chatserver.repository.UserRepository;
import com.yutsuki.chatserver.utils.ResultUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final FileRepository fileRepository;

    @Cacheable(value = "account", key = "#user.id")
    public Result<AccountResponse> getAccount(User user) {
        AccountResponse response = AccountResponse.fromEntity(user);
        return ResultUtils.success(response);
    }

    @CachePut(value = "account", key = "#user.id")
    public Result<AccountResponse> updateAccount(AccountUpdateRequest request, User user) throws BaseException {

        if (StringUtils.hasText(request.getAvatar())) {
            File file = fileService.getByUrlAndUser(request.getAvatar(), user);

            if (Objects.nonNull(user.getAvatar()) && !request.getAvatar().equals(user.getAvatar().getUrl())) {
                fileService.deleteByIdAndUser(user.getAvatar().getId(), user);
            }

            user.setAvatar(file);
        }

        if (StringUtils.hasText(request.getUsername())) {
            user.setUsername(request.getUsername());
        }

        userRepository.save(user);
        AccountResponse response = AccountResponse.fromEntity(user);

        return ResultUtils.successWithMessage(response, "Account updated successfully.");
    }

    public Result<Void> changePassword(ChangePasswordRequest request, User user) throws BaseException {

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            log.warn("ChangePassword-[block]:(invalid current password) userId:{}", user.getId());
            throw UserException.currentPasswordInvalid();
        }

        user.setPassword(passwordEncoder.encode(request.getLatestPassword()));
        userRepository.save(user);

        return ResultUtils.successWithMessage("Password changed successfully.");
    }

    @CachePut(value = "account", key = "#user.id")
    public Result<AccountResponse> deleteAvatar(User user) {

        if (Objects.nonNull(user.getAvatar())) {
            fileService.deleteByIdAndUser(user.getAvatar().getId(), user);
            user.setAvatar(null);
            userRepository.save(user);
        }

        AccountResponse response = AccountResponse.fromEntity(user);

        return ResultUtils.successWithMessage(response, "Avatar deleted successfully.");
    }

    public Result<Void> deleteAccount(User user) {

        // files
        List<File> files = fileRepository.findByUser(user);
        for (File file : files) {
            fileService.delete(file.getName());
        }

        userRepository.deleteAccountById(user.getId());
        return ResultUtils.successWithMessage("Account deleted successfully.");
    }
}
