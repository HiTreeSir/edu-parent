package cn.szxy.eduservice.service.impl;

import cn.szxy.eduservice.client.VodClient;
import cn.szxy.eduservice.entity.EduVideo;
import cn.szxy.eduservice.mapper.EduVideoMapper;
import cn.szxy.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    /**
     * 根据课程id删除课程小节
     * TODO 删除小节，删除对应小节视频
     * @param id
     */
    @Override
    public void removeVideoByCourseId(String id) {
        //根据课程id查询所有的小节视频id
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",id);
        //查询返回指定列，优化查询
        wrapper.select("video_source_id");
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);
        //将获取到的数据存到List<string>中
        List<String> list = new ArrayList<>();
        for (int i = 0; i < eduVideos.size(); i++) {
            EduVideo eduVideo = eduVideos.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)){
                list.add(videoSourceId);
            }
        }

        //调用vod服务删除远程视频
        //调用远程接口批量删除云端视频
        if(list.size() > 0){
            vodClient.removeMoreVideoByIds(list);
        }

        //删除小节信息
        QueryWrapper<EduVideo> wrapperV = new QueryWrapper<>();
        wrapperV.eq("course_id",id);
        baseMapper.delete(wrapperV);
    }
}
