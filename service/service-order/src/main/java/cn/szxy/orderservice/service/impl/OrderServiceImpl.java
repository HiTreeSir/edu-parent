package cn.szxy.orderservice.service.impl;

import cn.szxy.commonutils.orderVo.CourseInfoVoOrder;
import cn.szxy.commonutils.orderVo.UcenterMemberOrder;
import cn.szxy.orderservice.client.EduClient;
import cn.szxy.orderservice.client.UcenterClient;
import cn.szxy.orderservice.entity.Order;
import cn.szxy.orderservice.mapper.OrderMapper;
import cn.szxy.orderservice.service.OrderService;
import cn.szxy.orderservice.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Jack
 * @since 2020-09-14
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;
    /**
     * 根据课程id和用户id生成订单信息
     * @param courseId
     * @param userid
     * @return
     */
    @Override
    public String saveOrder(String courseId, String userid) {
        //远程调用课程服务，根据课程id获取课程信息
        CourseInfoVoOrder orderInfo = eduClient.getCourseInfoOrder(courseId);
        System.out.println(orderInfo);
        //远程调用用户服务，根据用户id获取用户信息
        UcenterMemberOrder userInfo = ucenterClient.getUserInfo(userid);

        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());//订单编号
        order.setCourseId(courseId);
        order.setCourseCover(orderInfo.getCover());
        order.setCourseTitle(orderInfo.getTitle());
        order.setTeacherName(orderInfo.getTeacherName());
        System.out.println(orderInfo.getTeacherName());
        order.setMemberId(userid);
        order.setMobile(userInfo.getMobile());
        order.setNickname(userInfo.getNickname());
        order.setStatus(0);
        order.setPayType(1);

        baseMapper.insert(order);
        return order.getOrderNo();
    }
}
