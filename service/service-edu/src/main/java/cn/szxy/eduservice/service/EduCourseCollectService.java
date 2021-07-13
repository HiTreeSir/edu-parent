package cn.szxy.eduservice.service;

import cn.szxy.eduservice.entity.EduCourseCollect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author Jack
 * @since 2020-09-13
 */
public interface EduCourseCollectService extends IService<EduCourseCollect> {
    /**
     * 查找用户收藏功能
     * @return
     */
    boolean getCourseCollect(String course, String userId);
}
