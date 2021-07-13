package cn.szxy.eduservice.controller.front;

import cn.szxy.commonutils.JwtUtils;
import cn.szxy.commonutils.R;
import cn.szxy.commonutils.orderVo.CourseInfoVoOrder;
import cn.szxy.eduservice.client.OrderClient;
import cn.szxy.eduservice.entity.EduCourse;
import cn.szxy.eduservice.entity.chapter.ChapterVo;
import cn.szxy.eduservice.entity.frontVo.CourseQueryFronVo;
import cn.szxy.eduservice.entity.frontVo.CourseWebVo;
import cn.szxy.eduservice.service.EduChapterService;
import cn.szxy.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.internal.$Gson$Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description = "前台页面课程管理")
@RestController
@RequestMapping("/eduservice/coursefront")
//@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    //根据条件查询课程
    @PostMapping("getFrontCourseList/{current}/{limit}")
    public R getFrontCourseList(@PathVariable Long current,@PathVariable Long limit,
                                @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                                @RequestBody(required = false) CourseQueryFronVo courseQuery){
        System.out.println(courseQuery);
        Page<EduCourse> page = new Page<>(current,limit);
        Map<String,Object> map = eduCourseService.getCourseFrontList(page,courseQuery);
        return R.ok().data("map",map);
    }

    //根据id查询前台页面课程的详细信息
    @ApiOperation(value = "根据ID查询课程")
    @GetMapping("getFrontCourseInfoById/{courseId}")
    public R getFrontCourseInfoById(@PathVariable String courseId, HttpServletRequest request){
        //根据id查询课程信息
        CourseWebVo courseWebVo = eduCourseService.getBaseCourseInfoById(courseId);

        //根据id查询章节和小节
        List<ChapterVo> chapterVideoByCourse = chapterService.getChapterVideoByCourseId(courseId);

        //调用远程方法查询订单支付状态
        //获取会员id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("memberId------------" + memberId);
        if(StringUtils.isEmpty(memberId)){
            return R.ok().data("courseInfo",courseWebVo).data("chapterVoList",chapterVideoByCourse).data("isbuy",false);
        }
        boolean buyCourse = orderClient.isBuyCourse(memberId, courseId);
        return R.ok().data("courseInfo",courseWebVo).data("chapterVoList",chapterVideoByCourse).data("isbuy",buyCourse);
    }

    @ApiOperation(value = "根据ID查询课程")
    @PostMapping("getCourseInfoOrder/{id}")
    public CourseInfoVoOrder getCourseInfoOrder(@PathVariable String id){
        CourseWebVo courseWebVo = eduCourseService.getBaseCourseInfoById(id);
        CourseInfoVoOrder order = new CourseInfoVoOrder();
        BeanUtils.copyProperties(courseWebVo,order);
        return order;
    }

    @ApiOperation(value = "根据课程id增加课程购买量")
    @GetMapping("updateBuyCount/{courseId}")
    public void updateBuyCount(@PathVariable String courseId){
        EduCourse course = eduCourseService.getById(courseId);
        course.setBuyCount(course.getBuyCount()+ 1);
        eduCourseService.updateById(course);
    }
}
