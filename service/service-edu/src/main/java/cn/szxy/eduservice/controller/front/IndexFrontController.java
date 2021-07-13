package cn.szxy.eduservice.controller.front;

import cn.szxy.commonutils.R;
import cn.szxy.eduservice.entity.EduCourse;
import cn.szxy.eduservice.entity.EduTeacher;
import cn.szxy.eduservice.service.EduCourseService;
import cn.szxy.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.Query;
import java.util.List;

@Api(description = "首页数据显示")
@RestController
@RequestMapping("/eduservice/indexFront")
//@CrossOrigin //解决跨域问题
public class IndexFrontController {


    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduTeacherService teacherService;

    /**
     * 查询首页的八条热门记录，四条热门名师数据
     * @return
     */
    @GetMapping("index")
    public R index(){
        //查询热门课程
        List<EduCourse> listEdu = courseService.selectHotCourse();
        //热门讲师
        List<EduTeacher> listTeacher = teacherService.selectHotTeacher();
        return R.ok().data("listEdu",listEdu).data("listTeacher",listTeacher);

    }
}
