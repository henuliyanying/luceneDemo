package com.example.mybatisdemo.utils;

import java.util.Timer;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/30 9:34
 */
public class Test {
    public static void main(String[] args){
        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(),1000);
    }
}
