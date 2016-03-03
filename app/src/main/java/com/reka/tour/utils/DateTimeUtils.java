package com.reka.tour.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    public static void main(String[] args) {

        DateTimeUtils obj = new DateTimeUtils();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.println("start :" + dateFormat.format(date));
        System.out.println("end :" + "2016-03-03 19:19:30");

        Calendar cal = Calendar.getInstance();
        System.out.println("start calendar :" + dateFormat.format(cal.getTime()));

        try {

            Date date1 = simpleDateFormat.parse("10/10/2013 11:30:10");
            Date date2 = simpleDateFormat.parse("13/10/2013 20:35:55");
            Date date3 = dateFormat.parse(dateFormat.format(date));
            Date date4 = dateFormat.parse("2016-03-03 19:55:30");

//            obj.printDifference(date1, date2);
            obj.printDifference(date3, date4);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public void printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);
    }

}