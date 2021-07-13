package cn.szxy.userservice.controller.api;


import cn.szxy.commonutils.JwtUtils;
import cn.szxy.commonutils.R;
import cn.szxy.servicebase.exception.CustomException;
import cn.szxy.userservice.entity.LoginVo;
import cn.szxy.userservice.entity.RegisterVo;
import cn.szxy.userservice.entity.UcenterMember;
import cn.szxy.userservice.service.UcenterMemberService;
import cn.szxy.userservice.utils.ConstantPropertiesUtil;
import cn.szxy.userservice.utils.HttpClientUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Jack
 * @since 2020-09-07
 */
@Api(description="微信开发平台")
//@CrossOrigin //解决跨域问题
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "本地测试方法")
    @GetMapping("callback")
    public String callback(String code,String state){
        //1 获取code值，临时票据，类似于验证码
        System.out.println(code);
        //2 拿着code请求 微信固定的地址，得到两个值 accsess_token 和 openid
        System.out.println(state);
        //向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code
        );

        ////请求这个拼接好的地址，得到返回两个值 accsess_token 和 openid
        String accessTokenInfo = null;
        try {
            accessTokenInfo  = HttpClientUtils.get(accessTokenUrl);
            System.out.println("resultUserInfo==========" + accessTokenInfo);
        } catch (Exception e) {
            throw new CustomException(20001, "获取access_token失败");
        }
        //从accessTokenInfo字符串获取出来两个值 accsess_token 和 openid
        //把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
        //使用json转换工具 Gson
        Gson gson = new Gson();
        HashMap<String, Object> accessTokenMap = gson.fromJson(accessTokenInfo, HashMap.class);
        String accsess_token = (String) accessTokenMap.get("access_token");
        String openid = (String) accessTokenMap.get("openid");
        //把扫描人信息添加数据库里面
        //判断数据表里面是否存在相同微信信息，根据openid判断
        UcenterMember member = memberService.getOne(new QueryWrapper<UcenterMember>().eq("openid", openid));
        if(member == null){
            System.out.println("新用户注册");

            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";

            String userInfoUrl = String.format(baseUserInfoUrl, accsess_token, openid);
            String resultUserInfo = null;

            try {
                //获取用户信息
                resultUserInfo =  HttpClientUtils.get(userInfoUrl);
                System.out.println(resultUserInfo);
            } catch (Exception e) {
                throw new CustomException(20001, "获取用户信息失败");
            }

            //使用json转换工具 Gson
            HashMap<String, Object> userInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String) userInfoMap.get("nickname");
            Double sex = (Double) userInfoMap.get("sex");
            System.out.println("性别----------------" + sex.toString());
            //判断用户性别
            Integer sex_user = null;
            if(1.0 == sex){
                sex_user = 1;
            }else if(2.0 == sex){
                sex_user = 2;
            }else{
                sex_user = 0;
            }
            String headimgurl = (String) userInfoMap.get("headimgurl");
            String openid_user = (String) userInfoMap.get("openid");

            //将新用户存储到数据库中
            UcenterMember ucenterMember = new UcenterMember();
            ucenterMember.setNickname(nickname);
            ucenterMember.setAvatar(headimgurl);
            ucenterMember.setOpenid(openid_user);
            ucenterMember.setSex(sex_user);
            memberService.save(ucenterMember);
        }
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());

        //因为端口号不同存在跨域问题，cookie不能跨域，所以这里使用url重写
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }


    @ApiOperation(value = "获取微信二维码")
    @GetMapping("login")
    public String getWxCode(){
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";


        // 回调地址 进行编码
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;

        try {
            redirectUrl = URLEncoder.encode(redirectUrl,"UTF-8");
        } catch (Exception e) {
            throw new CustomException(20001,"编码失败");
        }
        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        System.out.println("state = " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟
        //生成qrcodeUrl 对baseUrl中的%s进行赋值


        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                "imhelen"
        );
        return "redirect:" + qrcodeUrl;
    }


}

