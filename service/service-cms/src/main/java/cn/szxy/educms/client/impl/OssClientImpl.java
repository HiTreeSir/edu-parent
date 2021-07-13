package cn.szxy.educms.client.impl;

import cn.szxy.commonutils.R;
import cn.szxy.educms.client.OssClient;
import org.springframework.stereotype.Component;

@Component
public class OssClientImpl implements OssClient {
    @Override
    public R deleteFile(String fileName) {
        return R.error().message("删除图片出错了，熔断器。。。。。。。");
    }



}
