package cn.szxy.eduservice.service;

import cn.szxy.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author Jack
 * @since 2020-09-12
 */
public interface EduCommentService extends IService<EduComment> {

    Map<String, Object> selectPageByCourseId(Page<EduComment> pageParam, String courseId);

    /**
     * 获取课程评论数
     * @param courseId
     * @return
     */
    Integer getCommentTotal(String courseId);
}
