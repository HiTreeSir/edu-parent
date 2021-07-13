package cn.szxy.eduservice.service;

import cn.szxy.eduservice.entity.EduCourse;
import cn.szxy.eduservice.entity.frontVo.CourseQueryFronVo;
import cn.szxy.eduservice.entity.frontVo.CourseWebVo;
import cn.szxy.eduservice.entity.vo.CourseInfoVo;
import cn.szxy.eduservice.entity.vo.CoursePublishVo;
import cn.szxy.eduservice.entity.vo.CourseQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
public interface EduCourseService extends IService<EduCourse> {
    /**
     * 保存课程和课程详情信息
     * @param courseInfoVo
     */
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    /**
     * 根据id查询课程id
     * @param courseId
     * @return
     */
    CourseInfoVo getCourseInfoFormById(String courseId);

    /**
     * 修改课程方法
     * @param courseInfoVo
     */
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    /**
     * 根据id查询课程确认信息
     * 获取最终发布的数据
     * @param id
     * @return
     */
    CoursePublishVo getCoursePublishVoById(String id);

    /**
     * 最终发布
     * @param courseId
     */
    boolean publishCourseById(String courseId);

    /**
     * 分页查询
     * @param pageParam
     * @param courseQuery
     */
    void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery);

    /**
     * 删除课程信息
     * @param id
     */
    void removeCourseById(String id);

    /**
     * 查询热门课程
     * @return
     */
    List<EduCourse> selectHotCourse();

    /**
     * 根据讲师id查询讲师所讲课程
     * @param teacherId
     * @return
     */
    List<EduCourse> selectByTeacherId(String teacherId);

    /**
     * 根据条件查询课程信息
     * @param courseQuery
     * @return
     */
    Map<String, Object> getCourseFrontList(Page<EduCourse> page,CourseQueryFronVo courseQuery);

    /**
     * 根据id查询课程页面详细信息
     * @return
     */
    CourseWebVo getBaseCourseInfoById(String courseId);

    /**
     * 增加课程浏览量
     */
    void addViewCount(String id);

}
