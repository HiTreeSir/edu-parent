package cn.szxy.edumsm.controller;

import cn.szxy.commonutils.R;
import cn.szxy.edumsm.service.MsmService;
import cn.szxy.edumsm.utils.RandomUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(description = "阿里云对象服务接口")
@RestController
@RequestMapping("/edumsm/servicemsm")
//@CrossOrigin //解决跨域问题
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired  //springboot整合到rides模板
    private RedisTemplate<String, String> redisTemplate;


    @GetMapping("send/{phone}")
    public R sendAlyMsm(@PathVariable String phone){
        //查询缓存中是获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }

        //调用工具类随机生成六位数字的验证码
        code = RandomUtil.getSixBitRandom();

        Map<String,Object> param = new HashMap<>();
        param.put("code",code);
        boolean flag = msmService.sendSms(phone,param);

        if(flag){
            //发送成功
            //将验证码设置到rides中,设置有效时间 5分组
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.HOURS);
            return R.ok();
        }else {
            return R.error().message("短信发送失败");
        }

    }
}
