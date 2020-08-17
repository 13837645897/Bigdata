package com.atguigu.gmalllogger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.constans.GmallConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ypdstart
 * @Description:
 * @Date: 2020/8/14 19:15
 * @Version: 1.0
 */
@RestController
//注解在类上为类提供一个 属性名为 log 的 log4j 日志对象，提供默认构造方法
@Slf4j
public class LoggerController {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @RequestMapping("log")
    public String getLogger(@RequestParam("logString") String logString){
        //添加時間戳
        JSONObject jsonObject = JSON.parseObject(logString);
        jsonObject.put("ts",System.currentTimeMillis());
        //寫入日誌
        log.info(jsonObject.toString());
        //寫入kafka
        if ("startup".equals(jsonObject.getString("type"))){
            //寫入啟動日誌主題
            kafkaTemplate.send(GmallConstants.KAFKA_TOPIC_STARTUP,jsonObject.toString());
        } else {
            //寫入時間日誌主題
            kafkaTemplate.send(GmallConstants.KAFKA_TOPIC_EVENT,jsonObject.toString());
        }
        return "success";
    }
}
