package com.yutsuki.chatserver.utils;

public class MapUtils {

    /**
     * Calculates the bounding box offsets (latitude and longitude) based on a given latitude and radius.
     *
     * @param latitude the latitude at which to calculate the bounding box
     * @param radius   the radius in kilometers for the bounding box
     * @return an array containing the latitude offset and longitude offset
     */
    public static double[] calculateBoundingBox(double latitude, double radius) {
        double latOffset = radius / 111.32; // 111.32 km per degree of latitude
        double lngOffset = radius / (111.32 * Math.cos(Math.toRadians(latitude))); // Adjust for latitude
        return new double[]{
                latOffset,
                lngOffset
        };
    }
}