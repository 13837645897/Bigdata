package com.atguigu.publisher.gmallpublisher.service;

import java.util.Map;

/**
 * @Author: ypdstart
 * @Description:
 * @Date: 2020/8/17 12:57
 * @Version: 1.0
 */
public interface PublisherService {

    public Integer getDauTotal(String date);

    public Map getDauTotalHourMap(String date);

}
