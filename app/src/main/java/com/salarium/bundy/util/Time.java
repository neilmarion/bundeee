package com.salarium.bundy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    private static String FORMAT = "yyyy-MM-dd HH:mm";

    public static String convertUnixTimeToHumanDate(String unixTimeString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT);
        long unixTimeStamp = Long.parseLong(unixTimeString, 10);
        Date date = new Date(unixTimeStamp*1000);
        return dateFormat.format(date);
    }

    public static String getHumanDateForToday() {
        return new SimpleDateFormat(FORMAT).format(new Date());
    }
}
