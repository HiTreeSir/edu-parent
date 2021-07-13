package cn.szxy.userservice.service;

import cn.szxy.userservice.entity.LoginVo;
import cn.szxy.userservice.entity.RegisterVo;
import cn.szxy.userservice.entity.UcenterMember;
import cn.szxy.userservice.entity.UserFindVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Jack
 * @since 2020-09-07
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    /**
     * 登录方法
     * @param loginVo
     * @return
     */
    String login(LoginVo loginVo);

    /**
     * 注册功能
     * @param registerVo
     */
    void register(RegisterVo registerVo);

    UcenterMember getPhoneInfo(String phone);

    //根据日期查询某天注册人数
    Integer countRegister(String day);
    //待条件的分页查询
    void pageQuery(Page<UcenterMember> pageUser, UserFindVo userFindVo);
}
