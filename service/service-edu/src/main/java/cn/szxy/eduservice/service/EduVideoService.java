package cn.szxy.eduservice.service;

import cn.szxy.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
public interface EduVideoService extends IService<EduVideo> {
    /**
     * 根据课程id删除课程小节
     * @param id
     */
    void removeVideoByCourseId(String id);
}
