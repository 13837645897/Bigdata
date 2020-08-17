package com.atguigu.bean

/**
 * @Author: ypdstart
 * @Description: ${description}  
 * @Date:    2020/8/16 15:57
 * @Version:    1.0
*/
case class StartUpLog(
                       mid: String,
                       uid: String,
                       appid: String,
                       area: String,
                       os: String,
                       ch: String,
                       `type`: String,
                       vs: String,
                       var logDate: String,
                       var logHour: String,
                       var ts: Long
                     )
