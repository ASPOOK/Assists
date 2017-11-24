package com.aspook.assists.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeConverterUtil {

    private TimeConverterUtil() {

    }

    public static String utc2GMT(String utcTime) {
        String gmtTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = sdf.parse(utcTime);
            if (date != null) {
                gmtTime = sdf.parse(utcTime).toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return utcTime;
        }

        return gmtTime;
    }
}
