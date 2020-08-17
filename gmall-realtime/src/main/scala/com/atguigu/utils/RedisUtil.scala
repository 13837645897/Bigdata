package com.atguigu.utils

import java.util.Properties

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

/**
 * @Author: ypdstart
 * @Description: ${description}  
 * @Date:    2020/8/15 13:57
 * @Version:    1.0
*/
object RedisUtil {
  //初始化一个线程池
  var jedisPool:JedisPool = _
  def getJedisClint : Jedis = {
    if (jedisPool == null){
      //加载配置文件
      val properties: Properties = PropertiesUtil.load("config.properties")
      //获取配置文件
      val host: String = properties.getProperty("redis.host")
      val port: String = properties.getProperty("redis.port")
      //创建连接池环境
      val jedisPoolConfig = new JedisPoolConfig()
      //最大连接数
      jedisPoolConfig.setMaxTotal(100)
      //最大空闲
      jedisPoolConfig.setMaxIdle(20)
      //最小空闲
      jedisPoolConfig.setMinIdle(20)
      //忙碌时是否等待
      jedisPoolConfig.setBlockWhenExhausted(true)
      //忙碌时等待时长
      jedisPoolConfig.setMaxWaitMillis(500)
      //每次获取连接进行测试
      jedisPoolConfig.setTestOnBorrow(true)
      //创建线程池对象
      jedisPool = new JedisPool(jedisPoolConfig,host,port.toInt)
    }
    jedisPool.getResource //申请资源
  }

}
