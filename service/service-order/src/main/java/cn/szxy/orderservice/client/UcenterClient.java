package cn.szxy.orderservice.client;


import cn.szxy.commonutils.orderVo.UcenterMemberOrder;
import cn.szxy.orderservice.client.impl.UcenterClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-ucenter")
public interface UcenterClient {
    @GetMapping("/userservice/ucentermember/getUserInfo/{id}")
    public UcenterMemberOrder getUserInfo(@PathVariable("id") String id);
}
