package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * Validates that the start date is before the end date.
     *
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
     *
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

    public static String getDateFormat(String dateString, boolean is_start) {
        // Define regular expressions for the different date formats
        String yearPattern = "^\\d{4}$";
        String yearMonthPattern = "^\\d{4}-(0[1-9]|1[0-2])$";
        String fullDatePattern = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

        // Compile patterns
        Pattern yearPat = Pattern.compile(yearPattern);
        Pattern yearMonthPat = Pattern.compile(yearMonthPattern);
        Pattern fullDatePat = Pattern.compile(fullDatePattern);

        // Match input string with patterns
        Matcher yearMatcher = yearPat.matcher(dateString);
        Matcher yearMonthMatcher = yearMonthPat.matcher(dateString);
        Matcher fullDateMatcher = fullDatePat.matcher(dateString);

        // Return the corresponding format and complete the date if necessary
        if (yearMatcher.matches()) {
            return completeDate(dateString, "yyyy", is_start);
        } else if (yearMonthMatcher.matches()) {
            return completeDate(dateString, "yyyy-MM", is_start);
        } else if (fullDateMatcher.matches()) {
            return dateString; // Already a full date
        } else {
            return "Invalid format";
        }
    }

    private static String completeDate(String dateString, String format, boolean is_start) {
        switch (format) {
            case "yyyy":
                return is_start ? dateString + "-01-01" : dateString + "-12-31";
            case "yyyy-MM":
                if (is_start) {
                    return dateString + "-01";
                } else {
                    String[] parts = dateString.split("-");
                    int year = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int lastDay = getLastDayOfMonth(year, month);
                    return dateString + "-" + lastDay;
                }
            default:
                return dateString;
        }

    }

    private static int getLastDayOfMonth(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    return 29; // Leap year
                } else {
                    return 28;
                }
            default:
                throw new IllegalArgumentException("Invalid month: " + month);
        }
    }

    public static Date stringToDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateString, formatter);
            return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateString, e);
        }
    }

    public static String timestamp2DateStr(int timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            return true;
        }
        return false;
    }

    public static int daysInMonth(int month, int year) {
        switch (month) {
            case 2:
                return isLeapYear(year) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

    public static int daysFromYear0(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int totalDays = 0;

        // Adding days from complete years
        for (int y = 0; y < year; y++) {
            totalDays += isLeapYear(y) ? 366 : 365;
        }

        // Adding days from complete months of the current year
        for (int m = 0; m < month; m++) {
            totalDays += daysInMonth(m, year);
        }

        // Adding days of the current month
        totalDays += day;

        return totalDays;
    }

    public static String dateFromDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 0);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Add the number of days to the start date
        calendar.add(Calendar.DAY_OF_MONTH, days);

        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static int getDaysInMonth(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }

    public static int getDaysInYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            return 366;
        } else {
            return 365;
        }
    }

    public static int getDaysInYears(int year, int rangeYears) {
        int days = 0;
        if (rangeYears < 0) {
            for (int i = year + rangeYears; i < year; i++) {
                days += getDaysInYear(i);
            }
            days = days - 1;
        } else {
            for (int i = year; i <= year + rangeYears; i++) {
                days += getDaysInYear(i);
            }
        }

        return days;
    }

}
