package cn.szxy.eduservice.service.impl;

import cn.szxy.eduservice.entity.EduCourseCollect;
import cn.szxy.eduservice.mapper.EduCourseCollectMapper;
import cn.szxy.eduservice.service.EduCourseCollectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程收藏 服务实现类
 * </p>
 *
 * @author Jack
 * @since 2020-09-13
 */
@Service
public class EduCourseCollectServiceImpl extends ServiceImpl<EduCourseCollectMapper, EduCourseCollect> implements EduCourseCollectService {


    @Override
    public boolean getCourseCollect(String course, String userId) {
        QueryWrapper<EduCourseCollect> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",course);
        wrapper.eq("member_id",userId);
        Integer count = baseMapper.selectCount(wrapper);
        return count>0?true:false;
    }
}
