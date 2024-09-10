package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateHandler {

    private String startDateString;

    private String endDateString;

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
        if (this.startDate.isBefore(this.endDate) || this.startDate.isEqual(this.endDate)) {
            return (int) ChronoUnit.DAYS.between(this.startDate, this.endDate);
        } else {
            return -1;
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
            LocalDate newDate = this.startDate.plusDays(days);
            if (newDate.isAfter(this.endDate)) {
                return this.endDate.toString();
            }
            return newDate.toString();
        }

        return "";
    }

}
