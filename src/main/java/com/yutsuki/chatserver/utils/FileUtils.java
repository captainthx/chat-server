package com.yutsuki.chatserver.utils;


public class FileUtils {

    /**
     * @param fileExtension file extension like "jpg", "png", "mp3"
     * @return a random name with file extension
     */
    public static String generateName(String fileExtension) {
        return RandomUtils.notSymbol() + "." + fileExtension;
    }

    /**
     * @param contentType content type like "image/jpeg", "audio/mpeg"
     * @return the type of content type like "image", "audio"
     */
    public static String getType(String contentType) {
        return contentType.split("/")[0];
    }

    /**
     * @param contentType content type like "image/jpeg", "audio/mpeg"
     * @return the extension of content type like "jpeg", "mpeg"
     */
    public static String getExtension(String contentType) {
        var extension = contentType.split("/")[1];

        if (extension.equals("mpeg")) {
            extension = "mp3";
        }

        return extension;
    }

    /**
     * Removes the file extension from the given filename if it exists.
     * If the filename does not contain a dot or the dot is the first character, the original filename is returned.
     *
     * @param filename the filename from which the extension should be removed
     * @return the filename without its extension, or the original filename if no extension is found
     */
    public static String removeExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) { // ตรวจสอบว่ามี "." และไม่ใช่ตัวแรก
            return filename.substring(0, lastDotIndex);
        }
        return filename; // คืนค่าเดิมหากไม่มีจุด
    }
}
