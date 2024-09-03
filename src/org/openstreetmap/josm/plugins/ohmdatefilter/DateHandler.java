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

//    public DateHandler(String startDateString, String endDateString) {
//        this.startDateString = startDateString;
//        this.endDateString = endDateString;
//        this.startDate = getDateFromString(startDateString);
//        this.endDate = getDateFromString(endDateString);
//    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
//        this.startDate = getDateFromString(startDateString);
        this.startDate = LocalDate.parse(startDateString, formatter);

    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
        this.endDate = LocalDate.parse(endDateString, formatter);
//        this.endDate = getDateFromString(endDateString);
    }

    public LocalDate getStartDate() {
        System.out.println("===");

        System.out.println(this.startDate);

        return this.startDate;
    }

    public LocalDate getEndDate() {
        System.out.println("----");

        System.out.println(this.endDate);

        return this.endDate;
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
