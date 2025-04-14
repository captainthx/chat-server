package com.yutsuki.chatserver.controller;

import com.yutsuki.chatserver.common.BearerAuth;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.AccountUpdateRequest;
import com.yutsuki.chatserver.model.request.ChangePasswordRequest;
import com.yutsuki.chatserver.model.response.AccountResponse;
import com.yutsuki.chatserver.service.AccountService;
import com.yutsuki.chatserver.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
@BearerAuth
@Tag(name = "Account",description = "Account APIs")
public class AccountController {

    private final AccountService accountService;
    private final AuthService authService;

    @Operation(summary = "Get Account")
    @GetMapping
    public Result<AccountResponse> getAccount() throws BaseException {
        return accountService.getAccount(authService.getUser());
    }


    @Operation(
            summary = "Update account",
            description = """
                    <h3>errors</h3><br>
                    <li><b>name.invalid</b>: name is invalid</li>
                    <li><b>avatar.invalid</b>: Avatar is invalid</li>
                    """
    )
    @PutMapping
    public Result<AccountResponse> updateAccount(@Valid @RequestBody AccountUpdateRequest request) throws BaseException {
        return accountService.updateAccount(request, authService.getUser());
    }

    @PatchMapping("/change-password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) throws BaseException {
        return accountService.changePassword(request, authService.getUser());
    }

    @DeleteMapping("/avatar")
    public Result<AccountResponse> deleteAvatar() throws BaseException {
        return accountService.deleteAvatar(authService.getUser());
    }

    @DeleteMapping
    public Result<Void> deleteAccount() throws BaseException {
        return accountService.deleteAccount(authService.getUser());
    }

}
