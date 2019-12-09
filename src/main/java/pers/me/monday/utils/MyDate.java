package pers.me.monday.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyDate {

    public static String addDays(String start_date,int len){
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart;
        String end_date;
        Calendar calendar = new GregorianCalendar();
        try {
            dateStart = simpleDateFormat.parse(start_date);
            calendar.setTime(dateStart);
            calendar.add(Calendar.DATE, len);
            end_date = simpleDateFormat.format(calendar.getTime());
        }catch (ParseException e){
            MyLogger.log("MyDate.addDays","parseDateError",e.toString());
            return null;
        }
        return end_date;
    }



}
