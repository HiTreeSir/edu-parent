package cn.szxy.eduservice.controller;


import cn.szxy.commonutils.R;
import cn.szxy.eduservice.entity.EduCourse;
import cn.szxy.eduservice.entity.vo.CourseInfoVo;
import cn.szxy.eduservice.entity.vo.CoursePublishVo;
import cn.szxy.eduservice.entity.vo.CourseQuery;
import cn.szxy.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
@Api(description = "课程管理")
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin //解决跨域问题
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    /**
     * 课程列表查询+分页+条件查询
     * @return
     */
    @ApiOperation(value = "分页课程列表")
    @PostMapping("{page}/{limit}")
    public R getCourseList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
            CourseQuery courseQuery ){
        Page<EduCourse> pageParam = new Page<>(page, limit);
        courseService.pageQuery(pageParam,courseQuery);

        //获取出来的数据
        List<EduCourse> records = pageParam.getRecords();
        //获取总记录数
        long total = pageParam.getTotal();

        return R.ok().data("total",total).data("rows",records);
    }
    /**
     * 新增课程
     * @param courseInfoVo
     * @return
     */
    @ApiOperation(value = "新增课程")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(
            @ApiParam(name = "CourseInfoForm", value = "课程基本信息", required = true)
            @RequestBody CourseInfoVo courseInfoVo){
        String courseId = courseService.saveCourseInfo(courseInfoVo);
        if(!StringUtils.isEmpty(courseId)){
            return R.ok().data("courseId",courseId);
        }else {
            return R.error().message("保存失败");
        }

    }

    /**
     * 根据id查询课程信息
     * @param courseId
     * @return
     */
    @ApiOperation(value = "根据id查询课程")
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable("courseId") String courseId){
        System.out.println(courseId);
        CourseInfoVo courseInfoVo = courseService.getCourseInfoFormById(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    /**
     * 修改发布课程信息
     * @param courseInfoVo
     * @return
     */
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok().data("message","修改成功");
    }

    /**
     * 根据id查询课程确认信息
     * @param courseId
     * @return
     */
    @GetMapping("getPublishCourseInfo/{courseId}")
    public R getPublishCourseInfo(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId){
        CoursePublishVo coursePublishVoById = courseService.getCoursePublishVoById(courseId);
        return R.ok().data("coursePublish",coursePublishVoById);
    }

    /**
     * 最终发布
     * @param courseId
     * @return
     */
    @PostMapping("publishCourseById/{courseId}")
    public R publishCourseById(@PathVariable String courseId){
        EduCourse course = new EduCourse();
        course.setId(courseId);
        //设置发布状态 课程状态 Draft未发布 Normal已发布

        course.setStatus("Normal");
        courseService.updateById(course);
        return R.ok();
    }

    /**
     * 删除课程
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){

        courseService.removeCourseById(id);
        return  R.ok();
    }


    @ApiOperation(value = "增加课程浏览量")
    @GetMapping("addViewCount/{id}")
    public R addViewCount(@PathVariable String id){
        courseService.addViewCount(id);
        return R.ok();
    }
}

