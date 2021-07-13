package cn.szxy.eduservice.service;

import cn.szxy.eduservice.entity.EduTeacher;
import cn.szxy.eduservice.entity.vo.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-08-10
 */
public interface EduTeacherService extends IService<EduTeacher> {
    /**
     * 根据条件分页查询
     * @param pageParam
     * @param teacherQuery 查询对象，里面封装了查询条件
     */
    void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery);

    /**
     * 查询热门讲师，存入redis缓存
     * @return
     */
    List<EduTeacher> selectHotTeacher();

    /**
     * 前台页面查询讲师信息
     * @param page
     * @return
     */
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> page);
}
