package com.example.mybatisdemo.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/30 10:54
 */
@Component
public class MyTask {
    @Scheduled(fixedRate = 5000)
    public void testTaskFirst(){
        System.out.println("定时任务一：每五秒执行一次，当前时间："+ LocalTime.now());
    }

    @Scheduled(cron = "0 14 11 ? * *")
    public void testTaskSecond(){
        System.out.println("定时任务二：指定每天11:14执行，当前时间："+LocalTime.now());
    }
}
