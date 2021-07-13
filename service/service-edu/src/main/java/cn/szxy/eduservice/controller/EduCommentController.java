package cn.szxy.eduservice.controller;


import cn.szxy.commonutils.JwtUtils;
import cn.szxy.commonutils.R;
import cn.szxy.eduservice.client.UcenterClient;
import cn.szxy.eduservice.entity.EduComment;
import cn.szxy.eduservice.entity.UcenterMember;
import cn.szxy.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author Jack
 * @since 2020-09-12
 */
@Api(description = "前端评论功能管理控制器")
@RestController
@RequestMapping("/eduservice/comment")
//@CrossOrigin //解决跨域问题
public class EduCommentController {
    @Autowired
    private EduCommentService commentService;
    @Autowired
    private UcenterClient ucenterClient;


    //根据课程id查询评论列表
    @ApiOperation(value = "评论分页列表")
    @GetMapping("getCommentList/{page}/{limit}")
    public R getCommentList( @ApiParam(name = "page", value = "当前页码", required = true)
                                 @PathVariable Long page,

                             @ApiParam(name = "limit", value = "每页记录数", required = true)
                                 @PathVariable Long limit,
                             String courseId){
        System.out.println("courseId:-------------" + courseId);
        //创建分页工具类
        Page<EduComment> pageParam = new Page<>(page, limit);

        //根据课程id字段进行分页查询
        Map<String,Object> map = commentService.selectPageByCourseId(pageParam,courseId);

        return R.ok().data("map",map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request){
        //通过token获取用户id
        String id = JwtUtils.getMemberIdByJwtToken(request);
        //判断用户是否登录
        if(StringUtils.isEmpty(id)){
            return R.error().code(28004).message("请先登录");
        }

        comment.setMemberId(id);

        UcenterMember member = ucenterClient.getInfo(id);

        comment.setAvatar(member.getAvatar());
        comment.setNickname(member.getNickname());

        commentService.save(comment);
        return R.ok();
    }

    @ApiOperation(value = "根据id删除评论")
    @DeleteMapping("deleteCommentById/{id}")
    public R deleteCommentById(@PathVariable String id){
        commentService.removeById(id);
        return R.ok();
    }
    @ApiOperation(value = "获取课程总评论数")
    @GetMapping("getCommentTotal/{courseId}")
    public R getCommentTotal(@PathVariable String courseId){
        Integer total = commentService.getCommentTotal(courseId);
        return R.ok().data("total",total);
    }
}

