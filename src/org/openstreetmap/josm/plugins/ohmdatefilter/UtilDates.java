package org.openstreetmap.josm.plugins.ohmdatefilter;

public class UtilDates {

    public static String formatDate(String dateString) {
        String[] parts = dateString.split("-");
        if (parts.length == 1) {
            int year = Integer.parseInt(parts[0]);
            return formatYear(year) + "-01-01";
        } else if (parts.length == 2) {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            return formatYear(year) + "-" + String.format("%02d", month) + "-01";
        } else if (parts.length == 3) {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            return formatYear(year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
        } else {
            throw new IllegalArgumentException("Invalid date format");
        }
    }

    private static String formatYear(int year) {
        if (year < 0) {
            return String.format("-%04d", -year);
        } else {
            return String.format("%04d", year);
        }
    }
}
