package com.yutsuki.chatserver.utils;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static final String PASSWORD_REGEXP = "([a-zA-Z0-9_]{8,16})";

    public static boolean crossRange(String str, int min, int max) {
        int length = str.length();
        return length < min || length > max;
    }
}
