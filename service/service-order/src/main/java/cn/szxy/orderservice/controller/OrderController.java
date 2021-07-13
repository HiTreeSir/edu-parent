package cn.szxy.orderservice.controller;


import cn.szxy.commonutils.JwtUtils;
import cn.szxy.commonutils.R;
import cn.szxy.orderservice.client.EduClient;
import cn.szxy.orderservice.client.UcenterClient;
import cn.szxy.orderservice.entity.Order;
import cn.szxy.orderservice.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author Jack
 * @since 2020-09-14
 */
@RestController
@RequestMapping("/orderservice/order")
//@CrossOrigin //解决跨域问题
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 生成订单方法
     */
    @PostMapping("createOrder/{courseId}")
    public R save(@PathVariable String courseId, HttpServletRequest request){
        //获取tekon中的用户id
        String userid = JwtUtils.getMemberIdByJwtToken(request);
        //根据课程id和用户id生成订单,并返回订单编号
        String orderNo = orderService.saveOrder(courseId,userid);

        return R.ok().data("orderNo",orderNo);
    }

    /**
     * 根据id获取订单信息接口
     * @param orderId
     * @return
     */
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        Order orderInfo = orderService.getOne(new QueryWrapper<Order>().eq("order_no", orderId));
        return R.ok().data("items",orderInfo);
    }
    /**
     * 查询当前课程是否被购买
     */
    @GetMapping("isBuyCourse/{memberId}/{courseId}")
    public boolean isBuyCourse(@PathVariable String memberId,@PathVariable String courseId){
        //订单状态是1表示支付成功
        int count = orderService.count(new QueryWrapper<Order>().eq("member_id", memberId).eq("course_id", courseId).eq("status", 1));
        if(count>0) {
            return true;
        } else {
            return false;
        }
    }
}

