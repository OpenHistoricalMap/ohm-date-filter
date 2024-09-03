package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateHandler {

    private String startDateString;

    private String endDateString;
    private int rangeInDays;

    private LocalDate startDate;
    private LocalDate endDate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DateHandler() {
    }

    public DateHandler(String startDateString, String endDateString) {
        this.startDateString = startDateString;
        this.endDateString = endDateString;
        this.startDate = getDateFromString(startDateString);
        this.endDate = getDateFromString(endDateString);
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
        this.startDate = getDateFromString(startDateString);

    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
        this.endDate = getDateFromString(endDateString);

    }

    public LocalDate getDateFromString(String dateString) {
        LocalDate date = null;

        // Check the format of the date string and complete it to 'yyyy-MM-dd'
        if (dateString.matches("\\d{4}")) {  // Only year
            dateString += "-01-01";  // Complete to 'yyyy-01-01'
        } else if (dateString.matches("\\d{4}-\\d{2}")) {  // Year and month
            dateString += "-01";  // Complete to 'yyyy-MM-01'
        } else if (!dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {  // Invalid format
            System.out.println("Error: Format should be 'yyyy', 'yyyy-MM', or 'yyyy-MM-dd'.");
            return null;  // Return null for invalid formats
        }

        try {
            // Parse the completed date string to LocalDate
            date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Format should be 'yyyy-MM-dd'.");
            return null;
        }

        return date;
    }

    public int getRangeInDays() {
        if (this.startDate != null && this.endDate != null) {
            // Calculate and return the number of days between startDate and endDate
            return (int) ChronoUnit.DAYS.between(this.startDate, this.endDate);
        }

        // If startDate or endDate is null, return 0 to indicate no range
        return 0;
    }

    @Override
    public String toString() {
        return "DateHandler {"
                + "startDateString='" + startDateString + '\''
                + ", endDateString='" + endDateString + '\''
                + ", rangeInDays=" + getRangeInDays()
                + ", startDate=" + (startDate != null ? startDate.format(formatter) : "null")
                + ", endDate=" + (endDate != null ? endDate.format(formatter) : "null")
                + '}';
    }

    public String addDaysToStartDate(int days) {
        if (this.startDate != null && this.endDate != null) {
            // Add the specified number of days to the startDate
            LocalDate newDate = this.startDate.plusDays(days);

            // Ensure the new date does not exceed the endDate
            if (newDate.isAfter(this.endDate)) {
                // If the new date exceeds the end date, return the end date as a string
                return this.endDate.toString();
            }

            // Otherwise, return the new date as a string
            return newDate.toString();
        }

        // If startDate or endDate is null, return an empty string or handle as needed
        return "";
    }

//    public static void main(String[] args) {
//        System.out.println("======================");
//
//        DateHandler handler = new DateHandler("1990", "2000-04");
//        String startDatePlus = handler.addDaysToStartDate(200);
//        System.out.println(handler.toString());
//        System.out.println(startDatePlus);
//
//        System.out.println("======================");
//        handler.setEndDateString("1991");
//        System.out.println(handler.toString());
//        System.out.println(startDatePlus);
//
//    }
}