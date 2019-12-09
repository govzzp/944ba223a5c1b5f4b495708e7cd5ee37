package pers.me.monday.controller;


import pers.me.monday.controller.responseEntity.DataWrapper;
import pers.me.monday.service.CheckInService;
import pers.me.monday.utils.CheckCode;
import pers.me.monday.utils.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
public class CheckInController {
    //学生请求签到(返回验证码)
    @Autowired
    RedisTemplate<String,String> redisTemplate03;
    @RequestMapping(value="/v1/student/check-in/code",method= RequestMethod.GET)
    ResponseEntity<HashMap<String,String>> getCode(@RequestHeader("UID") String uid){
        var randomCode = 5739;
        var result = new HashMap<String,String>();
        var url = CheckCode.genericCode(randomCode);
        result.put("url",url);
        redisTemplate03.opsForValue().set("captcha:"+uid,""+randomCode);
        redisTemplate03.expire("captcha:"+uid,30, TimeUnit.SECONDS);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @Autowired
    RedisTemplate<String,String> redisTemplate00;

    @Autowired
    RedisTemplate<String,String> redisTemplate01;

    //学生请求签到(确认)
    @RequestMapping(value="/v1/student/check-in",method= RequestMethod.PUT)
    ResponseEntity<HashMap<String,Boolean>> checkIn(
            @RequestHeader("UID") String uid,
            @RequestParam("code") int code
    ){
        var path = "/v1/student/check-in";
        HashMap<String, Boolean> rep = new HashMap<>();
        var codeTrue = redisTemplate03.opsForValue().get("captcha:"+uid);
        if(codeTrue!=null&&(Integer.parseInt(codeTrue)==code)){
            var teacherId = (String)redisTemplate00.opsForHash().get("check-in-mem-student",uid);
            if(teacherId!=null){
                var status = (String)redisTemplate01.opsForHash().get("check-in-status:"+teacherId,uid);
                if(status!=null){
                    var statusSlice = status.split(":");
                    redisTemplate01.opsForHash().put(
                            "check-in-status:"+teacherId,
                            uid,
                            "已签到:"+statusSlice[1]+":"+statusSlice[2]
                    );
                    rep.put("accepted",true);
                }else{
                    rep.put("accepted",false);
                    MyLogger.log(path,"error","no status");
                }
            }else{
                rep.put("accepted",false);
                MyLogger.log(path,"error","no such check in member");
            }
        }else{
            MyLogger.log(path,"error","code not right or no code");
            rep.put("accepted",false);
        }
        return new ResponseEntity<>(rep, HttpStatus.OK);
    }


    //学生检查自己当前是否在签到队列

    @RequestMapping("/v1/student/check-in")
    ResponseEntity<HashMap<String,Object>> checkIfinCheckInQueue(
            @RequestHeader("UID")String uid
    ){
        var h = new HashMap<String,Object>();
        var teacherId = redisTemplate00.opsForHash().get("check-in-mem-student",uid);
        if (teacherId != null) {
            h.put("check-in-now", true);
            //=======<teacher_name>:
            // <course_id>:<course_name>:
            // <start_time>:<end_time>:
            // <week_day>:<week_no>
            String checkInInfo = (String)redisTemplate00.opsForHash().get("check-in-info",teacherId);
            if(checkInInfo==null){
                MyLogger.log("/v1/student/check-in","error","redis里面没有对应的签到信息");
                h.put("error-in-server","sorry");
                return new ResponseEntity<>(h, HttpStatus.OK);
            }else{
                var infoSlice = checkInInfo.split(":");
                h.put("course_id",infoSlice[1]);
                h.put("course_name",infoSlice[2]);
                h.put("start_time",infoSlice[3]);
                h.put("end_time",infoSlice[4]);
                h.put("week_day",infoSlice[5]);
                h.put("week_no",infoSlice[6]);
                return new ResponseEntity<>(h, HttpStatus.OK);
            }
        }
        h.put("check-in-now",false);
        return new ResponseEntity<>(h, HttpStatus.OK);
    }

    //教师发起签到
    @Autowired
    CheckInService checkInService;
    @RequestMapping(value="/v1/teacher/check-in",method= RequestMethod.POST)
    ResponseEntity<DataWrapper<ArrayList<HashMap<String,Object>>>> invokeCheckIn(
            @RequestHeader("UID") String uid,
            //====Body=====
            //  course_id
            //  week_no
            //  week_day
            //=============
            @RequestBody HashMap<String,Integer> body
    ) {
        int course_id = body.get("course_id");
        int week_no = body.get("week_no");
        int week_day = body.get("week_day");
        var list = checkInService.invokeCheckIn(Integer.parseInt(uid),week_no,week_day,course_id);
        if(list!=null){
            return new ResponseEntity<>(new DataWrapper<>(list), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }
    //教师显示当前正在签到的人员名单
    @RequestMapping(value="/v1/teacher/check-in/member",method= RequestMethod.GET)
    ResponseEntity<DataWrapper<ArrayList<HashMap<String,String>>>> currentCheckInMember(
            @RequestHeader("UID") String uid
    ){
        return new ResponseEntity<>(new DataWrapper<>(checkInService.checkInMemberNow(Integer.parseInt(uid))), HttpStatus.OK);
    }

}
