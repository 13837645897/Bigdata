package com.atguigu.app


import java.text.SimpleDateFormat
import java.util.Date

import com.alibaba.fastjson.JSON
import com.atguigu.bean.StartUpLog
import com.atguigu.constans.GmallConstants
import com.atguigu.handler.DauHandler
import com.atguigu.utils.MykafkaUtil
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.phoenix.spark._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @Author: ypdstart
 * @Description: ${description}
 * @Date:    2020/8/16 15:59
 * @Version:    1.0
*/
object DauApp {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf
    val sparkConf: SparkConf = new SparkConf().setAppName("DauApp").setMaster("local[*]")
    //2.创建StreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    //3.读取Kafka Start 主题数据
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = MykafkaUtil.getKafkaStream(GmallConstants.KAFKA_TOPIC_STARTUP, ssc)
    //4.将读取的数据转换成样例类对象
    val startUpStream: DStream[StartUpLog] = kafkaDStream.map(record => {
      //取出对应的值
      val value: String = record.value()
      //将kafka中的json字符串解析成样例类
      val startUpLog: StartUpLog = JSON.parseObject(value, classOf[StartUpLog])
      //取出时间戳字段解析给logDate和logHour赋值
      //声明解析格式
      val format = new SimpleDateFormat("yyyy-MM-dd HH")
      //获取样例类中的时间
      val ts: Long = startUpLog.ts
      //将ts按照日期格式进行解析
      val dateHour: String = format.format(new Date(ts))
      //取时间的日期和小时 logDate logHour 赋值
      val hour_day: Array[String] = dateHour.split(" ")
      startUpLog.logDate = hour_day(0)
      startUpLog.logHour = hour_day(1)
      startUpLog
    })
    //5.将数据传到redis进行跨批次去重
    val value: DStream[StartUpLog] = DauHandler.filterByRedis(startUpStream, ssc.sparkContext)
    //6.将数据传到redis进行同批次去重
    val value2: DStream[StartUpLog] = DauHandler.filterByGroup(value)
    value2.cache()
    //7.将数据写入到redis进行后续的查询准备
    DauHandler.saveMidToRedis(value2)
    //8.将数据保存到hbase中
    value2.foreachRDD(rdd =>{
      rdd.saveToPhoenix(
        "GMALL2020_DAU",
        classOf[StartUpLog].getDeclaredFields
          .map(_.getName.toUpperCase()),
        HBaseConfiguration.create(),
        Some("hadoop102,hadoop103,hadoop104:2181"))

    })

    //9.启动任务
    ssc.start()
    ssc.awaitTermination()


  }


}
