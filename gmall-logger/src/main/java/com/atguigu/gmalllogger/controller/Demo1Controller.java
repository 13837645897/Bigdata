package com.atguigu.gmalllogger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: ypdstart
 * @Description:
 * @Date: 2020/8/14 19:05
 * @Version: 1.0
 */
@Controller
public class Demo1Controller {
    @ResponseBody
    @RequestMapping("t1")
    public String testDemo(){
        return "hello demo";
    }

}
