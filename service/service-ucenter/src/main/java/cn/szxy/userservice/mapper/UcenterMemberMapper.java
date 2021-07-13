package cn.szxy.userservice.mapper;

import cn.szxy.userservice.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author Jack
 * @since 2020-09-07
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    // 根据日期查询某天注册人数
    Integer countRegisterDay(String day);
}
