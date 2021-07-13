package cn.szxy.eduservice.client;

import cn.szxy.eduservice.client.impl.OrderClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-order", fallback = OrderClientImpl.class)
public interface OrderClient {

    @GetMapping("/orderservice/order/isBuyCourse/{memberId}/{courseId}")
    public boolean isBuyCourse(@PathVariable("memberId") String memberId,
                               @PathVariable("courseId") String courseId);
}
