package com.example.mybatisdemo.utils;


import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/30 8:58
 */
public class TimerManager {
    //时间间隔
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    public TimerManager(){
        Calendar calendar = Calendar.getInstance();

        /**定制每日2点执行方法**/
        calendar.set(Calendar.HOUR_OF_DAY,2);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        //第一次执行定时任务的时间
        Date date = calendar.getTime();
        //如果第一次执行定时任务的时间 小于 当前时间
        //此时要在第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行
        if(date.before(new Date())){
            date = this.addDay(date,1);
        }

        Timer timer = new Timer();
//        Task task = new Task();
        NFDFlightDataTimerTask task = new NFDFlightDataTimerTask();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行
        timer.schedule(task,date,PERIOD_DAY);
    }

    //增加或减少天数
    public Date addDay(Date date,int num){
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH,num);
        return startDT.getTime();
    }
}

