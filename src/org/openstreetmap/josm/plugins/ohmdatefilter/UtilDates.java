package org.openstreetmap.josm.plugins.ohmdatefilter;

public class UtilDates {
    public static String isDateFormat(String dateString) {
        String[] parts = dateString.split("-");

        switch (parts.length) {
            case 1:
                return "year";
            case 2:
                return "month";
            case 3:
                return "day";
            default:
                return "Invalid date format";
        }
    }
}
