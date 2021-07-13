package cn.szxy.eduservice.service.impl;

import cn.szxy.eduservice.entity.EduCourse;
import cn.szxy.eduservice.entity.EduCourseDescription;
import cn.szxy.eduservice.entity.EduTeacher;
import cn.szxy.eduservice.entity.frontVo.CourseQueryFronVo;
import cn.szxy.eduservice.entity.frontVo.CourseWebVo;
import cn.szxy.eduservice.entity.vo.CourseInfoVo;
import cn.szxy.eduservice.entity.vo.CoursePublishVo;
import cn.szxy.eduservice.entity.vo.CourseQuery;
import cn.szxy.eduservice.mapper.EduCourseMapper;
import cn.szxy.eduservice.service.EduChapterService;
import cn.szxy.eduservice.service.EduCourseDescriptionService;
import cn.szxy.eduservice.service.EduCourseService;
import cn.szxy.eduservice.service.EduVideoService;
import cn.szxy.servicebase.exception.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
@Service
@Transactional //开启自动管理事务
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService courseDescriptionService; //课程描述service

    @Autowired
    private EduChapterService chapterService; //课程章节

    @Autowired
    private EduVideoService videoService;//课程小节注入

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //向课程表添加基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        System.out.println(eduCourse);
        int insert = baseMapper.insert(eduCourse);
        //判断插入是否成功
        if( 0 == insert){
            //添加失败
            throw new CustomException(20001,"课程信息添加失败");
        }
        //获取添加之后的课程信息id
        String cid = eduCourse.getId();
        //向课程描述表添加信息
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(cid);//添加基本信息表数据的id 形成一对一关系
        courseDescription.setDescription(courseInfoVo.getDescription());//添加描述信息
        courseDescriptionService.save(courseDescription);


        return cid;
    }

    /**
     * 通过课程id获取课程基本信息（添加课程页面）
     * @param courseId
     * @return
     */
    @Override
    public CourseInfoVo getCourseInfoFormById(String courseId) {
        System.out.println(courseId);
        //通过id对课程信息进行查询
        EduCourse eduCourse = baseMapper.selectById(courseId);
        System.out.println(eduCourse);
        //判断返回数据是否为null
        if(eduCourse == null){
            throw new CustomException(20001,"数据不存在");
        }
        //将课程的部分信息封装到courseInfoVo
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //查询课程简介信息
        EduCourseDescription eduCourseDescription = courseDescriptionService.getById(courseId);
        if(eduCourseDescription != null){
            //将课程简介填充到courseInfoVo中
            courseInfoVo.setDescription(eduCourseDescription.getDescription());
        }
        return courseInfoVo;
    }

    /**
     * 修改课程信息
     * @param courseInfoVo
     */
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int i = baseMapper.updateById(eduCourse);

        if(i == 0){
            throw new CustomException(20001,"修改课程信息失败");
        }
        //修改课程信息描述表
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo,eduCourseDescription);
        courseDescriptionService.updateById(eduCourseDescription);

    }

    /**
     * 根据id查询课程确认信息
     * 获取最终发布的数据
     * @param id
     * @return
     */
    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {

        return baseMapper.getPublishCourseInfo(id);
    }

    /**
     * 最终发布
     * @param courseId
     */
    @Override
    public boolean publishCourseById(String courseId) {
        EduCourse course = new EduCourse();
        course.setId(courseId);
        course.setStatus("");
        Integer i = baseMapper.updateById(course);
        return i != null &&i > 0;
    }

    /**
     * 分页查询
     * @param pageParam
     * @param courseQuery
     */
    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        //判断当前参数条件是否为空
        if(courseQuery == null){
            baseMapper.selectPage(pageParam,wrapper);
            return;
        }

        //获取条件查询参数
        String title = courseQuery.getTitle(); //课程id
        String oneSubject = courseQuery.getSubjectParentId();//一级分类
        String twoSubject = courseQuery.getSubjectId();//二级分类id
        String teacherId = courseQuery.getTeacherId();//讲师id

        //构建条件
        //判断参数是否为null
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        //讲师id
        if(!StringUtils.isEmpty(teacherId)){
            wrapper.eq("subject_id",teacherId);
        }
        //一级分类id
        if(!StringUtils.isEmpty(oneSubject)){
            wrapper.eq("subject_parent_id",oneSubject);
        }
        //二级分类id
        if(!StringUtils.isEmpty(twoSubject)){
            wrapper.eq("subject_id",twoSubject);
        }
        //执行查询
        baseMapper.selectPage(pageParam, wrapper);

    }

    /**
     * 删除课程信息
     * @param id
     */
    @Override
    public void removeCourseById(String id) {
        //根据课程id删除课程小节
        videoService.removeVideoByCourseId(id);
        //删除课程章节
        chapterService.removeChapterByCourseId(id);
        //删除课程描述
        courseDescriptionService.removeById(id);
        //删除课程
        int i = baseMapper.deleteById(id);
        if(i == 0){
            throw new CustomException(20001,"删除课程章节失败");
        }
    }

    /**
     * 查询热门课程并且存入redis缓存
     * @return
     */
    @Cacheable(value = "index",key = "'indexCourseList'")
    @Override
    public List<EduCourse> selectHotCourse() {
        QueryWrapper<EduCourse> wrapperEdu  = new QueryWrapper<>();
        wrapperEdu.orderByDesc("id");
        wrapperEdu.last("limit 8");
        List<EduCourse> eduCourses = baseMapper.selectList(wrapperEdu);
        return eduCourses;
    }
    /**
     * 根据讲师id查询讲师所讲课程
     * @param teacherId
     * @return
     */
    @Override
    public List<EduCourse> selectByTeacherId(String teacherId) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        //按照最后更新时间倒序排列
        wrapper.orderByDesc("gmt_modified");


        List<EduCourse> eduCourses = baseMapper.selectList(wrapper);
        return eduCourses;
    }

    /**
     * 根据条件查询课程信息
     * @param courseQuery
     * @return
     */
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> page,CourseQueryFronVo courseQuery) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断查询条件是否为null
        if(!StringUtils.isEmpty(courseQuery.getSubjectId())){
            wrapper.eq("subject_id",courseQuery.getSubjectId());
        }
        //一级分类
        if(!StringUtils.isEmpty(courseQuery.getSubjectParentId())){
            wrapper.eq("subject_parent_id",courseQuery.getSubjectParentId());
        }
        //二级分类

        //排序
        //销量降序排列
        if(!StringUtils.isEmpty(courseQuery.getBuyCountSort())){
            wrapper.orderByDesc("buy_count");
        }
        //销量降序排列
        if(!StringUtils.isEmpty(courseQuery.getGmtCreateSort())){
            wrapper.orderByDesc("gmt_create");
        }
        if(!StringUtils.isEmpty(courseQuery.getPriceSort())){
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(page,wrapper);
        //获取分页的各项数据
        long total = page.getTotal(); //总记录数
        List<EduCourse> records = page.getRecords(); //查询数据列表
        long pages = page.getPages(); //总页数
        long size = page.getSize(); //每页显示条数
        long current = page.getCurrent();  //当前页
        boolean hasNext = page.hasNext();  //是否具有下一页
        boolean hasPrevious = page.hasPrevious(); //是否具有上一页
        //封装查询到的数据
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("records",records);
        map.put("size",size);
        map.put("pages",pages);
        map.put("current",current);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);

        return map;
    }

    /**
     * 根据id查询课程页面详细信息
     * @return
     */
    @Override
    public CourseWebVo getBaseCourseInfoById(String courseId) {
        return baseMapper.getBaseCourseInfoById(courseId);
    }

    @Override
    public void addViewCount(String id) {
        baseMapper.addViewCount(id);
    }
}
