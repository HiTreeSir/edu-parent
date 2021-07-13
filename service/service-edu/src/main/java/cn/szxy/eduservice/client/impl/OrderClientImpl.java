package cn.szxy.eduservice.client.impl;

import cn.szxy.eduservice.client.OrderClient;
import cn.szxy.servicebase.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class OrderClientImpl implements OrderClient {
    @Override
    public boolean isBuyCourse(String memberId, String courseId) {
        throw new CustomException(20001,"查询订单信息失败");
    }
}
