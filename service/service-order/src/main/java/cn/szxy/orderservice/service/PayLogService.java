package cn.szxy.orderservice.service;

import cn.szxy.orderservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author Jack
 * @since 2020-09-14
 */
public interface PayLogService extends IService<PayLog> {
    /**
     * 根据订单信息，生成支付二维码
     * @return
     */
    Map createNative(String orderNo);

    /**
     * 查询订单状态
     * @param orderNo
     * @return
     */
    Map<String, String> queryPayStatus(String orderNo);

    /**
     * 添加支付记录。更新订单
     * @param map
     */
    void updateOrderStatus(Map<String, String> map);
}
