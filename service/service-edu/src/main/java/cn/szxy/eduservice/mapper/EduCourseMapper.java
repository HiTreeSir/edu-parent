package cn.szxy.eduservice.mapper;

import cn.szxy.eduservice.entity.EduCourse;
import cn.szxy.eduservice.entity.frontVo.CourseWebVo;
import cn.szxy.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    /**
     * 获取发布的查询信息
     * @param courseId
     * @return
     */
    public CoursePublishVo getPublishCourseInfo(String courseId);

    /**
     * 根据id查询课程详细信息
     * @param courseId
     * @return
     */
    CourseWebVo getBaseCourseInfoById(String courseId);

    /**
     * 增加课程浏览量
     */
    void addViewCount(String id);

}
