package cn.szxy.orderservice.client.impl;

import cn.szxy.commonutils.orderVo.CourseInfoVoOrder;
import cn.szxy.commonutils.orderVo.UcenterMemberOrder;
import cn.szxy.orderservice.client.EduClient;
import cn.szxy.orderservice.client.UcenterClient;
import cn.szxy.servicebase.exception.CustomException;
import org.springframework.stereotype.Component;

/**
 * 熔断器
 */
@Component
public class UcenterClientImpl implements UcenterClient {
    @Override
    public UcenterMemberOrder getUserInfo(String id) {
       throw new CustomException(20001,"用户信息查询失败");
    }
}
