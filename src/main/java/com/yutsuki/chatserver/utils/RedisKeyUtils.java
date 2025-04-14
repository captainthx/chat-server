package com.yutsuki.chatserver.utils;

public final class RedisKeyUtils {
    private RedisKeyUtils() {
    }

    public static String emailVerification(String email) {
        return "API:EMAIL_VERIFICATION:" + email;
    }

    public static String forgotPassword(String email) {
        return "API:FORGOT_PASSWORD:" + email;
    }
}
