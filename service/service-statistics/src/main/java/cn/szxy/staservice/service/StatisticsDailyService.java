package cn.szxy.staservice.service;

import cn.szxy.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author Jack
 * @since 2020-09-15
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    //统计当前日期注册人数
    void registerCount(String day);
    //图表显示，返回两部分数据，日期json数组，数量json数组
    Map showChart(String begin, String end, String type);
}
