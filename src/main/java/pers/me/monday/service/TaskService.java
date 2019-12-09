package pers.me.monday.service;


import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

@Service
public class TaskService {

    public void runATask(TimerTask task/*传入的方法*/,long delay/*延迟执行*/){
        Timer timer = new Timer();
        timer.schedule(task,delay);
    }
}

