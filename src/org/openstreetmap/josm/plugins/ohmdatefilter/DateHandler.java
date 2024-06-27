package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateHandler {

    private LocalDate date;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DateHandler() {
    }

    public DateHandler(String dateString) {
        setDate(dateString);
    }

    public void setDate(String dateString) {
        if (dateString.matches("\\d{4}")) {
            dateString += "-01-01"; 
        } else if (dateString.matches("\\d{4}-\\d{2}")) {
            dateString += "-01";
        }

        try {
            this.date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Format should be 'yyyy-MM-dd'.");
            this.date = null;
        }
    }

    public String getDate() {
        if (this.date != null) {
            return this.date.format(formatter);
        } else {
            return "Invalid date.";
        }
    }

    public int getDaysSinceYearZero() {
        if (this.date != null) {
            LocalDate yearZero = LocalDate.of(0, 1, 1);
            return (int) (this.date.toEpochDay() - yearZero.toEpochDay());
        } else {
            System.out.println("Invalid date.");
            return -1;
        }
    }

    public int getDaysAfterAdjustingYears(int years) {
        if (this.date != null) {
            LocalDate adjustedDate = (years > 0) ? this.date.plusYears(years) : this.date.minusYears(-years);
            return (int) (adjustedDate.toEpochDay() - LocalDate.of(0, 1, 1).toEpochDay());
        } else {
            System.out.println("Invalid date.");
            return -1;
        }
    }

    public String getDateAfterAdjustingYears(int years) {
        if (this.date != null) {
            LocalDate adjustedDate = (years > 0) ? this.date.plusYears(years) : this.date.minusYears(-years);
            return adjustedDate.format(formatter);
        } else {
            System.out.println("Invalid date.");
            return "Invalid date.";
        }
    }

    public int getDaysAfterAdjustingMonths(int months) {
        if (this.date != null) {
            LocalDate adjustedDate = (months > 0) ? this.date.plusMonths(months) : this.date.minusMonths(-months);
            return (int) (adjustedDate.toEpochDay() - LocalDate.of(0, 1, 1).toEpochDay());
        } else {
            System.out.println("Invalid date.");
            return -1;
        }
    }

    public String getDateAfterAdjustingMonths(int months) {
        if (this.date != null) {
            LocalDate adjustedDate = (months > 0) ? this.date.plusMonths(months) : this.date.minusMonths(-months);
            return adjustedDate.format(formatter);
        } else {
            System.out.println("Invalid date.");
            return "Invalid date.";
        }
    }

    public String getDateFromDaysSinceYearZero(int days) {
        LocalDate yearZero = LocalDate.of(0, 1, 1);
        LocalDate targetDate = yearZero.plusDays(days);
        return targetDate.format(formatter);
    }
}
