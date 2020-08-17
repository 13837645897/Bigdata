package com.atguigu.utils;

import java.util.Random;

/**
 * @Author: ypdstart
 * @Description:
 * @Date: 2020/8/14 15:02
 * @Version: 1.0
 */
import java.util.Date;
import java.util.Random;

public class RandomDate {

    Long logDateTime =0L;
    int maxTimeStep=0 ;

    public RandomDate (Date startDate , Date  endDate,int num) {
        Long avgStepTime = (endDate.getTime()- startDate.getTime())/num;
        this.maxTimeStep=avgStepTime.intValue()*2;
        this.logDateTime=startDate.getTime();
    }

    public  Date  getRandomDate() {
        int  timeStep = new Random().nextInt(maxTimeStep);
        logDateTime = logDateTime+timeStep;
        return new Date( logDateTime);
    }
}
