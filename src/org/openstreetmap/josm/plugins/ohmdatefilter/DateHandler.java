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

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
        this.startDate = LocalDate.parse(startDateString, formatter);

    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
        this.endDate = LocalDate.parse(endDateString, formatter);
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

  

  public int getRangeInDays() {
    if (this.startDate != null && this.endDate != null) {
        // Check if startDate is less than or equal to endDate
        if (this.startDate.isBefore(this.endDate) || this.startDate.isEqual(this.endDate)) {
            // Calculate and return the number of days between startDate and endDate
            return (int) ChronoUnit.DAYS.between(this.startDate, this.endDate);
        } else {
            throw new IllegalArgumentException("startDate should be before or equal to endDate.");
        }
    }
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

        return "";
    }

}
