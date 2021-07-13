package cn.szxy.orderservice.service.impl;

import cn.szxy.orderservice.client.EduClient;
import cn.szxy.orderservice.entity.Order;
import cn.szxy.orderservice.entity.PayLog;
import cn.szxy.orderservice.mapper.PayLogMapper;
import cn.szxy.orderservice.service.OrderService;
import cn.szxy.orderservice.service.PayLogService;
import cn.szxy.orderservice.utils.HttpClient;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author Jack
 * @since 2020-09-14
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EduClient eduClient;
    @Override
    public Map createNative(String orderNo) {
        try {
            //根据订单id编号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(wrapper);
            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", "wx74862e0dfcf69954"); //appit
            m.put("mch_id", "1558950191"); //商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr()); //生成随机字符串
            m.put("body", order.getCourseTitle()); //课程标题
            m.put("out_trade_no", orderNo); //订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");//订单价格
            m.put("spbill_create_ip", "127.0.0.1"); //作用范围
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE"); //支付类型

            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数,

            //设置xml格式的参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            //发送
            client.post();

            //3、返回第三方的数据,返回的时xml格式
            String xml = client.getContent();
            //讲xml格式内容转换成map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //4、封装返回结果集
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo); //订单号
            map.put("course_id", order.getCourseId()); //课程id
            map.put("total_fee", order.getTotalFee()); //订单价格
            map.put("result_code", resultMap.get("result_code")); //生成二维码的状态码
            map.put("code_url", resultMap.get("code_url")); //生成二维码的地址

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    /**
     * 查询订单状态
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 添加支付记录。更新订单
     * @param map
     */
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //获取订单编号
        String orderNo = map.get("out_trade_no");

        //根据订单编号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        //判断当前订单是否支付
        if(order.getStatus().intValue() == 1) return;
        //设置当前订单为已支付
        order.setStatus(1);
        //调用远程接口，更新课程购买量
        System.out.println("-----------------------" + order.getCourseId());
        eduClient.updateBuyCount(order.getCourseId());
        orderService.updateById(order);

        //记录支付日志
        //记录支付日志
        PayLog payLog=new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date()); //支付时间
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表
    }
}
