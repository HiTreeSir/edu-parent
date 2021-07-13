package cn.szxy.eduservice.service.impl;

import cn.szxy.eduservice.entity.EduComment;
import cn.szxy.eduservice.mapper.EduCommentMapper;
import cn.szxy.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author Jack
 * @since 2020-09-12
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {


    @Override
    public Map<String, Object> selectPageByCourseId(Page<EduComment> pageParam, String courseId) {
        if(!StringUtils.isEmpty(courseId)){
          baseMapper.selectPage(pageParam, new QueryWrapper<EduComment>().eq("course_id", courseId));
        }else{
          baseMapper.selectPage(pageParam,null);
        }
        List<EduComment> commentList = pageParam.getRecords();

        Map<String, Object> map = new HashMap<>();

        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return map;
    }

    @Override
    public Integer getCommentTotal(String courseId) {
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        Integer count = baseMapper.selectCount(wrapper);
        return count;
    }
}
