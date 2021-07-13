package cn.szxy.userservice.controller;


import cn.szxy.commonutils.JwtUtils;
import cn.szxy.commonutils.R;
import cn.szxy.commonutils.orderVo.UcenterMemberOrder;
import cn.szxy.userservice.entity.LoginVo;
import cn.szxy.userservice.entity.RegisterVo;
import cn.szxy.userservice.entity.UcenterMember;
import cn.szxy.userservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Jack
 * @since 2020-09-07
 */
@Api(description="用户管理中心")
//@CrossOrigin //解决跨域问题
@RestController
@RequestMapping("/userservice/ucentermember")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @ApiOperation("通过用户id获取用户信息")
    @PostMapping("getInfoUc/{id}")
    public UcenterMember getInfo(@PathVariable String id){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        return ucenterMember;
    }

    /*@ApiOperation("通过用户id获取用户信息")
    @GetMapping("getUserInfo/{id}")
    public R getUserInfo(@PathVariable String id){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        return R.ok().data("member",ucenterMember);

    }*/

    @ApiOperation("通过用户id获取用户信息")
    @GetMapping("getUserInfo/{id}")
    public UcenterMemberOrder getUserInfo(@PathVariable String id){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        UcenterMemberOrder order = new UcenterMemberOrder();
        BeanUtils.copyProperties(ucenterMember,order);
        return order;
    }

    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo LoginVo){

        String token = ucenterMemberService.login(LoginVo);
        return R.ok().data("token",token);
    }


    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("getLoginInfo")
    public R getLoginInfo(HttpServletRequest request){
        try {
            //通过jwt工具类中的方法获取request中的id
            String id = JwtUtils.getMemberIdByJwtToken(request);

            //通过id查询数据库中的对象
            UcenterMember member = ucenterMemberService.getById(id);

            return R.ok().data("loginInfo",member);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().message("获取用户信息失败");
        }
    }

    @ApiOperation(value = "手机号")
    @GetMapping("getPhoneInfo/{phone}")
    public R getPhoneInfo(@PathVariable String phone){
        UcenterMember member = ucenterMemberService.getPhoneInfo(phone);
        if(member == null){
            return R.ok().data("flag",true);
        }else {
            return R.ok().data("flag",false);
        }
    }

    @ApiOperation(value = "查询某一天的注册人数")
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
        //根据日期查询某天注册人数
        Integer count = ucenterMemberService.countRegister(day);
        return R.ok().data("countRegister",count);
    }
}

