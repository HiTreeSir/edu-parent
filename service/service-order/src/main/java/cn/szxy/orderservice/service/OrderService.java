package cn.szxy.orderservice.service;

import cn.szxy.orderservice.client.EduClient;
import cn.szxy.orderservice.client.UcenterClient;
import cn.szxy.orderservice.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Jack
 * @since 2020-09-14
 */
public interface OrderService extends IService<Order> {

    /**
     * 根据课程id和用户id生成订单信息
     * @param courseId
     * @param userid
     * @return
     */
    String saveOrder(String courseId, String userid);
}
