package com.atguigu.gmallpulisher.mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: ypdstart
 * @Description:
 * @Date: 2020/8/18 18:18
 * @Version: 1.0
 */
public interface OrderMapper {
    //查询当日订单总金额
    public Double selectOrderAmountTotal(String date);

    //查询当日订单金额的分时统计
    public List<Map> selectOrderAmountHourMap(String date);
}
