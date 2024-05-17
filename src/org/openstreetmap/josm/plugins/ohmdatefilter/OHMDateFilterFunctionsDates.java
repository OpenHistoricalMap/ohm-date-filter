package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class OHMDateFilterFunctionsDates {

    /**
     * Validates that the start date is before the end date.
     * @param startDate The starting date.
     * @param endDate The ending date.
     * @return true if the start date is before the end date, false otherwise.
     */
    public static boolean validateDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return false; // Validation fails if either date is null
        }
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return !startLocalDate.isAfter(endLocalDate);
    }

    /**
     * Calculates the difference between two dates.
     * @param startDate The starting date.
     * @param endDate The ending date.
     * @return A string describing the difference in years, months, and days.
     */
    public static String calculateDateDifference(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return "Invalid dates provided";
        }
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Period period = Period.between(startLocalDate, endLocalDate);
        return String.format("%d years, %d months, and %d days", period.getYears(), period.getMonths(), period.getDays());
    }
}
