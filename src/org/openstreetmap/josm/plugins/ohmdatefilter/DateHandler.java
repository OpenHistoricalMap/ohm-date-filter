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
            dateString += "-01-01"; // Completa el año con "-01-01" si solo se proporciona el año
        } else if (dateString.matches("\\d{4}-\\d{2}")) {
            dateString += "-01"; // Completa el año y el mes con "-01" si solo se proporciona el año y el mes
        }

        try {
            this.date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error: El formato de fecha debe ser 'yyyy-MM-dd'.");
            this.date = null;
        }
    }

    public String getDate() {
        if (this.date != null) {
            return this.date.format(formatter);
        } else {
            return "Fecha no válida.";
        }
    }

    public int getDaysSinceYearZero() {
        if (this.date != null) {
            LocalDate yearZero = LocalDate.of(0, 1, 1);
            return (int) (this.date.toEpochDay() - yearZero.toEpochDay());
        } else {
            System.out.println("Fecha no válida.");
            return -1;
        }
    }

    public int getDaysAfterAdjustingYears(int years) {
        if (this.date != null) {
            LocalDate adjustedDate = (years > 0) ? this.date.plusYears(years) : this.date.minusYears(-years);
            return (int) (adjustedDate.toEpochDay() - LocalDate.of(0, 1, 1).toEpochDay());
        } else {
            System.out.println("Fecha no válida.");
            return -1;
        }
    }

    public String getDateAfterAdjustingYears(int years) {
        if (this.date != null) {
            LocalDate adjustedDate = (years > 0) ? this.date.plusYears(years) : this.date.minusYears(-years);
            return adjustedDate.format(formatter);
        } else {
            System.out.println("Fecha no válida.");
            return "Fecha no válida.";
        }
    }

    public int getDaysAfterAdjustingMonths(int months) {
        if (this.date != null) {
            LocalDate adjustedDate = (months > 0) ? this.date.plusMonths(months) : this.date.minusMonths(-months);
            return (int) (adjustedDate.toEpochDay() - LocalDate.of(0, 1, 1).toEpochDay());
        } else {
            System.out.println("Fecha no válida.");
            return -1;
        }
    }

    public String getDateAfterAdjustingMonths(int months) {
        if (this.date != null) {
            LocalDate adjustedDate = (months > 0) ? this.date.plusMonths(months) : this.date.minusMonths(-months);
            return adjustedDate.format(formatter);
        } else {
            System.out.println("Fecha no válida.");
            return "Fecha no válida.";
        }
    }

    public String getDateFromDaysSinceYearZero(int days) {
        LocalDate yearZero = LocalDate.of(0, 1, 1);
        LocalDate targetDate = yearZero.plusDays(days);
        return targetDate.format(formatter);
    }
}
