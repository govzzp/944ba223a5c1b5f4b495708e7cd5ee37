package pers.me.monday.controller;

import pers.me.monday.service.TaskService;
import pers.me.monday.utils.MD5;
import pers.me.monday.utils.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//============ token timer============//
@RestController
public class TestController {

    @RequestMapping("/task")
    String haveATask(TaskService timeService){
        timeService.runATask(new TimerTask() {
            public void run() {
                Thread ct = Thread.currentThread();
                System.out.println("thread id: "+ct.getId()+"start");
                System.out.println("thread name: "+ct.getName());
            }
        },20000);
        return "running";
    }




    @Autowired
    RedisTemplate<String,String> redisTemplate02;


    //模拟返回token
    @RequestMapping("/v1/token")
    ResponseEntity<HashMap<String,String>> genericToken(@RequestParam("type") String type, @RequestParam("id") int id)  {
        MyLogger.log("/v1/token","id",Integer.toString(id),"type",type);
        var tokenHashMap = new HashMap<String,String>();

        var tokenString = UUID.randomUUID().toString();
        String sessionHash = null;
        try {
            sessionHash = MD5.generic(tokenString);
        }
        catch (NoSuchAlgorithmException e){
            System.out.println(e.toString());
        }

        tokenHashMap.put("token","Basic "+sessionHash+"-"+id);

        if(type.equals("student") || type.equals("teacher")){
            var key = "session-"+type+":"+ id;
            redisTemplate02.opsForValue().set(key,sessionHash);
            redisTemplate02.expire(key,7, TimeUnit.DAYS);//设置七天的有效时间
            return new ResponseEntity<>(tokenHashMap, HttpStatus.OK);
        }else{
            var errorHashMap = new HashMap<String,String>();
            errorHashMap.put("error","no such type");
            return new ResponseEntity<>(errorHashMap, HttpStatus.OK);
        }
    }
}
