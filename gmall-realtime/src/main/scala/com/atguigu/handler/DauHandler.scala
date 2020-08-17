package com.atguigu.handler

import java.time.LocalDate
import java.util

import com.atguigu.bean.StartUpLog
import com.atguigu.utils.RedisUtil
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import redis.clients.jedis.Jedis

/**
 * @Author: ypdstart
 * @Description: ${description}
 * @Date:    2020/8/16 16:02
 * @Version:    1.0
*/
object DauHandler {
  def saveMidToRedis(value2: DStream[StartUpLog]) ={
    //将两次去重后的数据写入到redis中
    value2.foreachRDD(RDD =>{
      RDD.foreachPartition(iter =>{
        //a.获取连接
        val jedisClint: Jedis = RedisUtil.getJedisClint
        //b.操作数据
        iter.foreach(startLog =>{
          val key = s"dau:${startLog.logDate}"
          jedisClint.sadd(key,startLog.mid)
        })
        //c.归还连接
        jedisClint.close()
      })
    })
  }

  //同批次去重
  def filterByGroup(value: DStream[StartUpLog]):DStream[StartUpLog]={
    //a.转换结构
    val midDateToStartLogDStream: DStream[(String, StartUpLog)] = value.map(startLog => {
      (s"${startLog.mid}-${startLog.logDate}", startLog)
    })
    //按照key进行分组
    val midDateToStartLogDStreamIter: DStream[(String, Iterable[StartUpLog])] = midDateToStartLogDStream.groupByKey()
    //组内取时间戳的最小的一串数据
    val value1: DStream[(String, List[StartUpLog])] = midDateToStartLogDStreamIter.mapValues(iter => {
      iter.toList.sortWith(_.ts < _.ts).take(1)
    })
    //对数据进行亚平处理
    value1.flatMap {
      case (_, list) => {
        list
      }
    }
  }
  //异批次处理
  def filterByRedis(startUpStream: DStream[StartUpLog], sc: SparkContext): DStream[StartUpLog]= {
     //将每个批次获取一次redis中的Set集合数据，广播到executor
     //无状态转换算子
    val value: DStream[StartUpLog] = startUpStream.transform(rdd => {
      val jedisClint: Jedis = RedisUtil.getJedisClint
      val now: String = LocalDate.now().toString
      val midSet: util.Set[String] = jedisClint.smembers(s"dau:$now")//传本地时间就是当天时间
      val midSetBC: Broadcast[util.Set[String]] = sc.broadcast(midSet)
      jedisClint.close()
      //进行广播变量进行去重
      rdd.filter(startLog => {
        !midSetBC.value.contains(startLog.mid)
      })}
    )
    println(value)
    value
  }



}
