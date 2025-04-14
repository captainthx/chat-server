package com.yutsuki.chatserver.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.model.Result;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Arrays;

@ControllerAdvice
public class ErrorAdviser {

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        if (ex.getBindingResult().getFieldError() == null) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(Result.builder()
                            .success(false)
                            .error("request.invalid")
                            .build());
        }

        final String field = ex.getBindingResult().getFieldError().getField();
        final String error = field + ".invalid";
        final String message = field + " " + ex.getBindingResult().getFieldError().getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(Result.builder()
                        .success(false)
                        .error(error)
                        .message(message)
                        .build());
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid Request";
        String fieldName = "unknown"; // ค่าเริ่มต้นกรณีไม่สามารถระบุฟิลด์ได้

        // ตรวจสอบว่ามาจาก InvalidFormatException และเป็น Enum หรือไม่
        if (ex.getCause() instanceof InvalidFormatException cause) {
            Class<?> targetType = cause.getTargetType();

            // ดึงชื่อฟิลด์จาก path ของ Exception
            if (!cause.getPath().isEmpty()) {
                fieldName = cause.getPath().get(0).getFieldName();
            }

            // ถ้าเป็น Enum ก็แสดงค่าที่ถูกต้องของ Enum
            if (targetType.isEnum()) {
                Object[] enumValues = targetType.getEnumConstants();
                errorMessage = fieldName + " must be one of " + Arrays.toString(enumValues);
            }
        }

        final String error = fieldName + ".invalid";

        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(Result.builder()
                        .success(false)
                        .error(error)
                        .message(errorMessage)
                        .build());
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<?>> handleMethodArgumentTypeMismatchException() {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(Result.builder()
                        .success(false)
                        .error("request.invalid")
                        .build());
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Result<?>> handleMaxSizeException() {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(Result.builder()
                        .success(false)
                        .error("file.size.exceeded")
                        .build());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthException.class, JwtValidationException.class})
    public ResponseEntity<Result<?>> handleAuthException(AuthException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Result.builder()
                        .success(false)
                        .error(e.getMessage())
                        .message("You are not authorized to access this resource.")
                        .build());
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Result<?>> handleBaseException(BaseException e) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(Result.builder()
                        .success(false)
                        .error(e.getMessage())
                        .build());
    }
}