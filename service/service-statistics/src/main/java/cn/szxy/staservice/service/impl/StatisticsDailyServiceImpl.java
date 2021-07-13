package cn.szxy.staservice.service.impl;

import cn.szxy.staservice.client.UcenterClient;
import cn.szxy.staservice.entity.StatisticsDaily;
import cn.szxy.staservice.mapper.StatisticsDailyMapper;
import cn.szxy.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author Jack
 * @since 2020-09-15
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    private UcenterClient ucenterClient;


    //统计当前日期注册人数
    @Override
    public void registerCount(String day) {
        //删除已存在的统计对象
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        //获取统计信息
        //注册人数
        Integer countRegister = (Integer) ucenterClient.countRegister(day).getData().get("countRegister");
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO

        //创建统计对象
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(countRegister); //当前注册人数
        daily.setLoginNum(loginNum);    //登录人数
        daily.setCourseNum(courseNum);  //每日新增课程数
        daily.setVideoViewNum(videoViewNum); //每日播放视频数
        daily.setDateCalculated(day);//当前查询日期
        baseMapper.insert(daily);
    }

    /**
     * 图表显示，返回两部分数据，日期json数组，数量json数组
     * @param begin 起始日期
     * @param end   结束日期
     * @param type  查询类型
     * @return
     */
    @Override
    public Map showChart(String begin, String end, String type) {
        //根据条件查询对应数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        //时间查询
        wrapper.between("date_calculated",begin,end);
        //根据type查询对应的字段
        wrapper.select(type,"date_calculated");
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

        //因为返回有两部分数据：日期 和 日期对应数量
        //前端要求数组json结构，对应后端java代码是list集合
        //创建两个list集合，一个日期list，一个数量list
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();

        //遍历查询所有数据list集合，进行封装
        for (int i = 0; i < staList.size(); i++) {
            StatisticsDaily daily = staList.get(i);
            //封装日期
            date_calculatedList.add(daily.getDateCalculated());

            //封装对应数量
            switch(type){
                case "login_num": //登录人数
                    numDataList.add(daily.getLoginNum());
                    break;
                case "register_num": //注册人数
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num": //观看视频人数
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":  //课程增加数量
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        //对map进行封装返回
        Map<String,Object> map = new HashMap<>();
        map.put("date_calculatedList",date_calculatedList);
        map.put("numDataList",numDataList);
        return map;
    }
}
