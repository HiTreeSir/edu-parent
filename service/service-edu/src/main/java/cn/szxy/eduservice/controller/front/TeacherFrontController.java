package cn.szxy.eduservice.controller.front;

import cn.szxy.commonutils.R;
import cn.szxy.eduservice.entity.EduCourse;
import cn.szxy.eduservice.entity.EduTeacher;
import cn.szxy.eduservice.service.EduCourseService;
import cn.szxy.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "前台页面讲师管理")
@RestController
@RequestMapping("/eduservice/teacherfront")
//@CrossOrigin //解决跨域问题
public class TeacherFrontController {
    @Autowired
    private EduTeacherService eduTeacherService;
    @Autowired
    private EduCourseService courseService;

    @ApiOperation("讲师分页查询")
    @GetMapping("getTeacherFrontList/{current}/{limit}")
    public R getTeacherFrontList(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit){
        //创建分页工具类
        Page<EduTeacher> page = new Page<>(current, limit);
        //返回结果
        Map<String,Object> map =  eduTeacherService.getTeacherFrontList(page);
        return R.ok().data("map",map);
    }

    /**
     * 根据id查询讲师信息和讲师所讲的全部课程
     * @param teacherId
     * @return
     */
    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId){
        //根据id查询讲师信息
        EduTeacher teacher = eduTeacherService.getById(teacherId);

        //根据id查询讲师课程信息
        List<EduCourse> courseList = courseService.selectByTeacherId(teacherId);

        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }

}
