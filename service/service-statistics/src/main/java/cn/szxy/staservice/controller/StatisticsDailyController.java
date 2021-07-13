package cn.szxy.staservice.controller;


import cn.szxy.commonutils.R;
import cn.szxy.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author Jack
 * @since 2020-09-15
 */
@RestController
@RequestMapping("/staservice/sta")
//@CrossOrigin //解决跨域问题
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService statisticsDailyService;

    /**
     * 统计某一天的注册人数，登录，观看视频，新增课程数的数据，生成数据
     * @param day
     * @return
     */
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day){
        statisticsDailyService.registerCount(day);
        return R.ok();
    }

    /**
     * 图表显示，返回两部分数据，日期json数组，数量json数组
     */
    @GetMapping("showChart/{begin}/{end}/{type}")
    public R showChart(@PathVariable String begin,@PathVariable String end,@PathVariable String type){
        Map map = statisticsDailyService.showChart(begin,end,type);
        return R.ok().data("mapChart",map);
    }
}

