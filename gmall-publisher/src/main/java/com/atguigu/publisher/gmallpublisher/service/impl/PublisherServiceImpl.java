package com.atguigu.publisher.gmallpublisher.service.impl;

import com.atguigu.publisher.gmallpublisher.mapper.DauMapper;
import com.atguigu.publisher.gmallpublisher.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ypdstart
 * @Description:
 * @Date: 2020/8/17 12:58
 * @Version: 1.0
 */
@Service
public class PublisherServiceImpl implements PublisherService {
    @Autowired
    private DauMapper dauMapper;

    //获取日活总数
    @Override
    public Integer getDauTotal(String date) {
        return dauMapper.selectDauTotal(date);
    }

    //获取日活分时数据
    @Override
    public Map getDauTotalHourMap(String date) {

        //1.查询Phoenix
        List<Map> list = dauMapper.selectDauTotalHourMap(date);

        //2.创建Map用于存放调整结构后的数据
        HashMap<String, Long> result = new HashMap<>();

        //3.调整结构
        for (Map map : list) {
            result.put((String) map.get("LH"), (Long) map.get("CT"));
        }

        //4.返回数据
        return result;
    }
}
