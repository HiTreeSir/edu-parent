package cn.szxy.educms.client;

import cn.szxy.commonutils.R;
import cn.szxy.educms.client.impl.OssClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
//fallback属性，当触发熔断器是，调用改属性当中的方法
@FeignClient(value = "service-oss")
public interface OssClient {


    @DeleteMapping("/eduoss/edufile/deleteFile")
    public R deleteFile(@RequestParam("fileName") String fileName);

}
