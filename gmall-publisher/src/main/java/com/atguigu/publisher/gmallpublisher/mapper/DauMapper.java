package com.atguigu.publisher.gmallpublisher.mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: ypdstart
 * @Description:
 * @Date: 2020/8/17 11:21
 * @Version: 1.0
 */
public interface DauMapper {
    //获取Phoenix表中的日活总数
    public Integer selectDauTotal(String date);

    //获取Phoenix表中的日活分时数据
    public List<Map> selectDauTotalHourMap(String date);

}
