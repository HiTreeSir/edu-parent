package cn.szxy.eduservice.controller.front;


import cn.szxy.commonutils.R;
import cn.szxy.eduservice.entity.EduCourseCollect;
import cn.szxy.eduservice.service.EduCourseCollectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程收藏 前端控制器
 * </p>
 *
 * @author Jack
 * @since 2020-09-13
 */
@RestController
@RequestMapping("/eduservice/coursecollect")
//@CrossOrigin
public class EduCourseCollectController {
    @Autowired
    private EduCourseCollectService collectService;

    @GetMapping("getcourseCollect/{course}/{userId}")
    public R courseCollect(@PathVariable String course, @PathVariable String userId){
       boolean flag =  collectService.getCourseCollect(course,userId);
        System.out.println(flag);
       return R.ok().data("falg",flag);
    }

    @GetMapping("addCourseCollect/{course}/{userId}")
    public R addCourseCollect(@PathVariable String course, @PathVariable String userId){
        EduCourseCollect collect = new EduCourseCollect();
        collect.setCourseId(course);
        collect.setMemberId(userId);
        collectService.save(collect);
        return R.ok();
    }

    @DeleteMapping("deleteCourseCollect/{course}/{userId}")
    public R deleteCourseCollect(@PathVariable String course, @PathVariable String userId){
        collectService.remove(new QueryWrapper<EduCourseCollect>().eq("course_id",course).eq("member_id",userId));
        return R.ok();
    }


}

