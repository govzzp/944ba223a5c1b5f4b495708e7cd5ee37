package pers.me.monday.controller;


import pers.me.monday.model.custom.ApiHelper;
import pers.me.monday.service.DateService;
import pers.me.monday.utils.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

@RestController
public class GeneralInfoController {
    //返回当前学期
    @RequestMapping(value="/v1/info/semester",method= RequestMethod.GET)
    ResponseEntity<HashMap<String,String>> getCurrentSemester(){
        var hashMap = new HashMap<String,String>();
        Calendar now = Calendar.getInstance();
        if((now.get(Calendar.MONTH) + 1)>=2&&(now.get(Calendar.MONTH) + 1)<=7){
            hashMap.put("semester",now.get(Calendar.YEAR)+"-1");
        }else{
            hashMap.put("semester",now.get(Calendar.YEAR)+"-1");
        }
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    @Autowired
    DateService dateService;
    //返回当前周和星期几
    @RequestMapping(value="/v1/info/week",method= RequestMethod.GET)
    ResponseEntity<HashMap<String,Long>> getCurrentWeek(){
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        var formattedDate = simpleDateFormat.format(now.getTime());
        MyLogger.log("v1/info/week","date-formatted",formattedDate);
        var weekCount = dateService.getWeek(formattedDate);
        var hashMap = new HashMap<String,Long>();

        var weekDay = dateService.getWeekDay(formattedDate);
        hashMap.put("weekNo",weekCount);
        hashMap.put("weekDay",weekDay);
        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    //返回当前api
    @RequestMapping(
            value={"/v1/","/","/v1"},
            method={RequestMethod.GET},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<ApiHelper> help(){
        ApiHelper helper = new ApiHelper();
        return new ResponseEntity<ApiHelper>(helper, HttpStatus.OK);
    }
}
