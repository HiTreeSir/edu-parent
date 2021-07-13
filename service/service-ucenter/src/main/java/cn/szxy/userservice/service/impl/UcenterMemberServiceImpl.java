package cn.szxy.userservice.service.impl;

import cn.szxy.commonutils.JwtUtils;
import cn.szxy.commonutils.MD5;
import cn.szxy.servicebase.exception.CustomException;
import cn.szxy.userservice.entity.LoginVo;
import cn.szxy.userservice.entity.RegisterVo;
import cn.szxy.userservice.entity.UcenterMember;
import cn.szxy.userservice.entity.UserFindVo;
import cn.szxy.userservice.mapper.UcenterMemberMapper;
import cn.szxy.userservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Jack
 * @since 2020-09-07
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /**
     * 登录方法
     * @param loginVo
     * @return
     */
    @Override
    public String login(LoginVo loginVo) {
        //获取手机号和密码
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //判断手机号是否为null
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new CustomException(20001,"手机号或密码不能为空");
        }


        //通过手机号查询用户对象
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember userInfo = baseMapper.selectOne(wrapper);

        //判断用户信息是否为null
        if(userInfo == null){
            throw new CustomException(20001,"用户信息不完善");
        }

        //判断密码是否与用户信息一致,
        // 使用MD5工具类对用户数据的密码进行加密处理，在与数据库中的加密密码进行对比
        if(!MD5.encrypt(password).equals(userInfo.getPassword())){
            throw new CustomException(20001,"密码错误");
        }
        //校验是否被禁用
        if(userInfo.getIsDisabled()){
            throw new CustomException(20001,"此账户被禁用，请联系管理员");
        }

        //通过用户id和用户名使用JWT生成token字符串
        String token = JwtUtils.getJwtToken(userInfo.getId(),userInfo.getNickname());
        return token;
    }

    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息进行校验
        String nickname = registerVo.getNickname(); //会员名
        String mobile = registerVo.getMobile();  //手机号
        String password = registerVo.getPassword(); //密码
        String code = registerVo.getCode();  //验证码

        //参数校验
        if( StringUtils.isEmpty(nickname)||
            StringUtils.isEmpty(mobile) ||
            StringUtils.isEmpty(password) ||
            StringUtils.isEmpty(code)){
            throw new CustomException(20001,"参数不能为null");
        }

        //校验验证码
        //从redis中查询验证码
        String mobleCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(mobleCode)){
            throw new CustomException(20001,"验证码错误");
        }

        //查询手机号是否已被注册
        Integer count = baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if(count > 0){
            throw new CustomException(20001,"手机号已被注册，请换个手机号");
        }

        //添加注册信息到数据库
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setIsDisabled(false);
        //密码加密
        member.setPassword(MD5.encrypt(password));
        member.setNickname(nickname);
        //默认头像
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getPhoneInfo(String phone) {
        UcenterMember member = baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("mobile", phone));

        return member;
    }

    //根据日期查询某天注册人数
    @Override
    public Integer countRegister(String day) {
        return baseMapper.countRegisterDay(day);
    }

    @Override
    public void pageQuery(Page<UcenterMember> pageUser, UserFindVo userFindVo) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();

        if(userFindVo == null){
            baseMapper.selectPage(pageUser,wrapper);

        }

        //获取查询条件
        String nickname = userFindVo.getNickname(); //用户名
        Boolean isDisabled = userFindVo.getIsDisabled(); //是否禁用
        String mobile = userFindVo.getMobile(); //手机号

        if(!StringUtils.isEmpty(nickname)){
            wrapper.like("nickname",nickname);
        }

        if(isDisabled != null){
            wrapper.eq("is_disabled",isDisabled);
        }

        if(!StringUtils.isEmpty(mobile)){
            wrapper.eq("mobile",mobile);
        }


        wrapper.orderByDesc("gmt_create");
        //执行分页查询
        baseMapper.selectPage(pageUser,wrapper);
    }
}
