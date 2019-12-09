package pers.me.monday.service;

import pers.me.monday.utils.MyLogger;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//日期转换
@Service
public class DateService {
    //得到第几周星期几是几月记号
    public String getDate(int weekNo,int weekDay){
        Calendar now = Calendar.getInstance();
        Calendar before = Calendar.getInstance();
        if((now.get(Calendar.MONTH) + 1)>=2&&(now.get(Calendar.MONTH) + 1)<=7){
            before.set(now.get(Calendar.YEAR),Calendar.FEBRUARY,25);
        }else{
            before.set(now.get(Calendar.YEAR),Calendar.SEPTEMBER,1);
        }

        var offset = before.get(Calendar.DAY_OF_WEEK);
        if(offset>1){
            offset-=1;
        }else{
            offset=7;
        }
        before.add(Calendar.DATE,weekNo*7-offset+weekDay);
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(before.getTime());
    }
    //得到目标日期处在学期的第几周
    public long getWeek(String date) {

        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart;
        Calendar now = Calendar.getInstance();
        try {
            dateStart = simpleDateFormat.parse(date);
            now.setTime(dateStart);
        }catch(ParseException e){
            MyLogger.log("DateService.getWeek()","error","parseDateString");
        }
        Calendar that = Calendar.getInstance();
        long betweenDays;
        if((now.get(Calendar.MONTH) + 1)>=2&&(now.get(Calendar.MONTH) + 1)<=7){
        that.set(now.get(Calendar.YEAR),Calendar.FEBRUARY,25);
        }else{
        that.set(now.get(Calendar.YEAR),Calendar.SEPTEMBER,1);
        }
        var offset = that.get(Calendar.DAY_OF_WEEK);
        if(offset>1){
            offset-=1;
        }else{
            offset=7;
        }
        betweenDays = (now.getTimeInMillis()-that.getTimeInMillis())/(1000*3600*24);
        betweenDays += offset;
        return betweenDays/7;
    }

    //返回指定日期是周几// 1,2,3,4,5,6,7(星期日)
    public long getWeekDay(String date){

        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart;
        Calendar now = Calendar.getInstance();
        try {
            dateStart = simpleDateFormat.parse(date);
            now.setTime(dateStart);
        }catch(ParseException e){
            MyLogger.log("DateService.getWeek()","error","parseDateString");
        }
        int index = now.get(Calendar.DAY_OF_WEEK);
        if(index>=2){
            return index-1;
        }else{
            return 7;
        }
    }
}


