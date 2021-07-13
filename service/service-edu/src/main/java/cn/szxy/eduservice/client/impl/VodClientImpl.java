package cn.szxy.eduservice.client.impl;

import cn.szxy.commonutils.R;
import cn.szxy.eduservice.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 当远程调用的服务器宕机时，则调用改实现类的方法
 * 出错之后执行的方法
 */
@Component
public class VodClientImpl implements VodClient{

    //出错之后执行的方法
    @Override
    public R removeVideoById(String videoId) {
        return R.error().message("删除视频出错了，熔断器。。。。。。。");
    }

    @Override
    public R removeMoreVideoByIds(List<String> videoIdList) {
        return R.error().message("批量删除视频出错了，熔断器。。。。。。。");
    }
}
