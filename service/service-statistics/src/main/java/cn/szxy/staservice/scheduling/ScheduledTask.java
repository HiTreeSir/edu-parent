package cn.szxy.staservice.scheduling;

import cn.szxy.staservice.service.StatisticsDailyService;
import cn.szxy.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService dailyService;


    /**
     * 每天凌晨1点执行定时,把前一天的数据进行查询，添加，生成数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        //获取上一天的日期
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        dailyService.registerCount(day);
    }
}
