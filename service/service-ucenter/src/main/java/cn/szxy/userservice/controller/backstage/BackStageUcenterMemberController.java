package cn.szxy.userservice.controller.backstage;


import cn.szxy.commonutils.R;
import cn.szxy.userservice.entity.UcenterMember;
import cn.szxy.userservice.entity.UserFindVo;
import cn.szxy.userservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Jack
 * @since 2020-09-07
 */
@Api(description="后台用户管理中心")
@RestController
@RequestMapping("/userservice/backgrounduser")
public class BackStageUcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @ApiOperation(value = "学院待条件的分页查询")
    @PostMapping ("pageStrdent/{current}/{limit}")
    public R pageUser(@ApiParam(name = "current",value = "当前页码",required = true)
                          @PathVariable Long current,
                      @ApiParam(name = "limit",value = "每页显示记录数",required = true)
                          @PathVariable Long limit,
                      @ApiParam(name = "userFindVo", value = "查询对象", required = false)
                      @RequestBody(required = false) UserFindVo userFindVo){
        //创建分页工具类
        Page<UcenterMember> pageUser = new Page<UcenterMember>(current,limit);
        ucenterMemberService.pageQuery(pageUser,userFindVo);

        Map<String,Object> pages = new HashMap<String,Object>();
        pages.put("total",pageUser.getTotal());//封装总记录数map集合中
        pages.put("rows",pageUser.getRecords());//封装所有查询信息进list中
        return R.ok().data(pages);
    }


    @ApiOperation("根据id查询学员信息")
    @GetMapping("getStudentById/{id}")
    public R getStudentById(@PathVariable String id){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        return R.ok().data("student",ucenterMember);
    }

    @ApiOperation("根据id删除学员信息")
    @DeleteMapping("deleteStudentById/{id}")
    public R deleteStudentById(@PathVariable String id){
        ucenterMemberService.removeById(id);
        return R.ok();
    }


    @ApiOperation("根据id修改学员信息")
    @PostMapping("updateStudentById")
    public R updateStudentById(@RequestBody(required = true) UcenterMember ucenterMember){
        ucenterMemberService.updateById(ucenterMember);
        return R.ok();
    }
}

