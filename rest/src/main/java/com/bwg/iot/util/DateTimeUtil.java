package com.bwg.iot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing and printing date and time.
 */
public final class DateTimeUtil {

    public static final String DATE_FORMAT_STR = "MM/dd/yyyy";
    public static final String TIME_FORMAT_STR = "HH:mm:ss";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT_STR);

    private DateTimeUtil() {
        // utility class - no public constructor needed
    }

    public static Map<Integer, Integer> parseTime(final String timeStr) throws ParseException {
        final Date date = TIME_FORMAT.parse(timeStr);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final int second = calendar.get(Calendar.SECOND);

        final Map<Integer, Integer> map = new HashMap<>();
        map.put(Calendar.HOUR_OF_DAY, hour);
        map.put(Calendar.MINUTE, minute);
        map.put(Calendar.SECOND, second);

        return map;
    }

    public static Map<Integer, Integer> parseDate(final String dateStr) throws ParseException {
        final Date date = DATE_FORMAT.parse(dateStr);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        final Map<Integer, Integer> map = new HashMap<>();
        map.put(Calendar.DAY_OF_MONTH, day);
        map.put(Calendar.MONTH, month);
        map.put(Calendar.YEAR, year);

        return map;
    }
}
