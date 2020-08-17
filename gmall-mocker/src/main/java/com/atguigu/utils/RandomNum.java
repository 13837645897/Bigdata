package com.atguigu.utils;

/**
 * @Author: ypdstart
 * @Description:
 * @Date: 2020/8/14 15:04
 * @Version: 1.0
 */
import java.util.Random;

public class RandomNum {
    public static int getRandInt(int fromNum,int toNum){
        return   fromNum+ new Random().nextInt(toNum-fromNum+1);
    }
}
