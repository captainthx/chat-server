package com.yutsuki.chatserver.exception;

public class FileException extends BaseException {

    public FileException(String code) {
        super("file." + code);
    }

    public static BaseException notFound() {
        return new FileException("notFound");
    }

    public static FileException uploadFailed() {
        return new FileException("upload.failed");
    }

    public static BaseException uploadUnsupported() {
        return new FileException("upload.unsupported");
    }
}
