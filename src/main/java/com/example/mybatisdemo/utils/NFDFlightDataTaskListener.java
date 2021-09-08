package com.example.mybatisdemo.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/30 10:03
 */
public class NFDFlightDataTaskListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce){
        new TimerManager();
    }

    public void contextDestroyed(ServletContextEvent sce){

    }
}
