package com.atguigu.utils

import java.io.InputStreamReader
import java.util.Properties

/**
 * @Author: ypdstart
 * @Description: 加载配置文件
 * @Date:    2020/8/15 13:56
 * @Version:    1.0
*/
object PropertiesUtil {
  def load(propertieName : String):Properties = {
    val properties = new Properties()
    properties.load(new InputStreamReader(
      Thread.currentThread()
        .getContextClassLoader
        .getResourceAsStream(propertieName),"UTF-8"))
    properties
  }

}

