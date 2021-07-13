package cn.szxy.orderservice.client.impl;

import cn.szxy.commonutils.orderVo.CourseInfoVoOrder;
import cn.szxy.orderservice.client.EduClient;
import cn.szxy.servicebase.exception.CustomException;
import org.springframework.stereotype.Component;

/**
 * 熔断器
 */
@Component
public class EduClientImpl implements EduClient {
    @Override
    public CourseInfoVoOrder getCourseInfoOrder(String id) {
        throw new CustomException(20001,"课程信息查询失败，熔断器");
    }

    @Override
    public void updateBuyCount(String courseId) {
        throw new CustomException(20001,"购买量更新失败，熔断器");
    }
}
