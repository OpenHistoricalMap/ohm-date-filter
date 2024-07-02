package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

class DateCompose {

    private String dateString;
    private int numValue;

    public DateCompose(String dateString, int daysFromYearZero) {
        this.dateString = dateString;
        this.numValue = daysFromYearZero;
    }

    public String getDateString() {
        return dateString;
    }

    public int getNumValue() {
        return numValue;
    }

    @Override
    public String toString() {
        String val = "DateCompose{"
                + "dateString='" + dateString + '\''
                + ", numValue=" + numValue
                + '}';
        System.out.println(val);
        return val;
    }

}

public class DateHandler {

    private LocalDate date;
    private String str_date;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String format;

    public DateHandler() {
    }

    public DateHandler(String dateString) {
        setDate(dateString);
    }

    public void setDate(String dateString) {
        this.str_date = dateString;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (dateString.matches("\\d{1,4}")) {
            this.format = "year";
            dateString = String.format("%04d-01-01", Integer.parseInt(dateString));
        } else if (dateString.matches("\\d{1,4}-\\d{1,2}")) {
            this.format = "month";
            String[] parts = dateString.split("-");
            dateString = String.format("%04d-%02d-01", Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } else if (dateString.matches("\\d{1,4}-\\d{1,2}-\\d{1,2}")) {
            this.format = "day";
            String[] parts = dateString.split("-");
            dateString = String.format("%04d-%02d-%02d", Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        } else {
            this.date = null;
            return;
        }

        try {
            this.date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format.");
            this.date = null;
        }
    }

    public String getDate() {
        if (this.date != null) {
            return this.date.format(formatter);
        } else {
            return null;
        }
    }

    public int getCurrentYearFromYearZero() {
        if (this.date != null) {
            LocalDate yearZero = LocalDate.of(0, 1, 1);
            return (int) ChronoUnit.YEARS.between(yearZero, this.date);
        }
        return 0;
    }

    public DateCompose getRangeYear(int value) {
        if ("year".equals(this.format) && this.date != null) {
            LocalDate targetDate;
            if (value >= 0) {
                targetDate = this.date.plusYears(value);
            } else {
                targetDate = this.date.minusYears(Math.abs(value));
            }
            long yearsFromYearZero = ChronoUnit.YEARS.between(LocalDate.of(0, 1, 1), targetDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
            return new DateCompose(targetDate.format(formatter), (int) yearsFromYearZero);
        }
        return new DateCompose("", 0);
    }

    public int getCurrentMonthFromYearZero() {
        if (this.date != null) {
            LocalDate yearZero = LocalDate.of(0, 1, 1);
            return (int) ChronoUnit.MONTHS.between(yearZero, this.date);
        }
        return 0;
    }

    public int getMonthsSinceYearZero(LocalDate date) {
        LocalDate startDate = LocalDate.of(0, 1, 1);
        return (int) ChronoUnit.MONTHS.between(startDate, date);
    }

    public DateCompose getRangeMonth(int months) {
        if ("month".equals(this.format) && this.date != null) {
            LocalDate targetDate;
            if (months < 0) {
                targetDate = this.date.minusMonths(Math.abs(months));
            } else {
                targetDate = this.date.plusMonths(months);
            }
            long monthsFromYearZero = ChronoUnit.MONTHS.between(LocalDate.of(0, 1, 1), targetDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

            return new DateCompose(targetDate.format(formatter), (int) monthsFromYearZero);
        }
        return new DateCompose("", 0);
    }

    public DateCompose getRangeDays(int days) {
        if (this.date != null) {
            LocalDate targetDate;
            if (days >= 0) {
                targetDate = this.date.plusDays(days);
            } else {
                targetDate = this.date.minusDays(Math.abs(days));
            }
            long daysFromYearZero = ChronoUnit.DAYS.between(LocalDate.of(0, 1, 1), targetDate);
            return new DateCompose(targetDate.toString(), (int) daysFromYearZero);
        }
        return new DateCompose("", 0);
    }

    public int getCurrentDaysFromYearZero() {
        if (this.date != null) {
            LocalDate yearZero = LocalDate.of(0, 1, 1);
            return (int) ChronoUnit.DAYS.between(yearZero, this.date);
        }
        return 0;
    }

    public String getFormat() {
        return this.format;
    }

    public String getFormattedDateString(int value) {
        LocalDate targetDate = LocalDate.of(0, 1, 1);
        DateTimeFormatter formatter;

        if ("year".equals(this.format)) {
            targetDate = targetDate.plusYears(value);
            formatter = DateTimeFormatter.ofPattern("yyyy");
        } else if ("month".equals(this.format)) {
            targetDate = targetDate.plusMonths(value);
            formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        } else if ("day".equals(this.format)) {
            targetDate = targetDate.plusDays(value);
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else {
            return "";
        }

        return targetDate.format(formatter);
    }
}
