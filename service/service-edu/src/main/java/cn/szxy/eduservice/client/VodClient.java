package cn.szxy.eduservice.client;

import cn.szxy.commonutils.R;
import cn.szxy.eduservice.client.impl.VodClientImpl;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 在service-edu的client包里面创建熔断器的实现类
 */
@Component
//fallback属性，当触发熔断器是，调用改属性当中的方法
@FeignClient(value = "service-vod",fallback = VodClientImpl.class) //注解用于指定从哪个服务中调用功能 ，名称与被调用的服务名保持一致。
public interface VodClient {

    @DeleteMapping("/eduvod/video/removeVideoById/{videoId}")
    public R removeVideoById(
            @PathVariable("videoId") String videoId);

    @DeleteMapping("/eduvod/video/removeMoreVideoByIds")
    public R removeMoreVideoByIds(
            @ApiParam(name = "videoIdList", value = "云端视频id集合", required = true)
            @RequestParam("videoIdList") List<String> videoIdList);
}
