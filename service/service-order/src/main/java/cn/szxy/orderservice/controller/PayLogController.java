package cn.szxy.orderservice.controller;


import cn.szxy.commonutils.R;
import cn.szxy.orderservice.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author Jack
 * @since 2020-09-14
 */
@RestController
@RequestMapping("/orderservice/paylog")
//@CrossOrigin //解决跨域问题
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    /**
     * 根据订单信息，生成支付二维码
     * @return
     */
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
       Map map = payLogService.createNative(orderNo);
        System.out.println("---订单"+ map +"查询----");
       return R.ok().data("items",map);
    }


    /**
     * 根据订单号查询订单支付状态
     */
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        //调用查询接口
       Map<String,String> map = payLogService.queryPayStatus(orderNo);

       if(map == null){
           return R.error().message("未找到改订单信息！");
       }

       if("SUCCESS".equals(map.get("trade_state"))){
           //支付成功，修改订单表的状态，添加支付记录
           payLogService.updateOrderStatus(map);
           return R.ok().message("支付成功");
       }
        return R.ok().code(25000).message("支付中");
    }
}

