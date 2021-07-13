package cn.szxy.vod.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 常量类，读取配置文件application.properties中的配置
 *
 * 用spring的 InitializingBean接口 的 afterPropertiesSet() 来初始化配置信息，
 * 这个方法将在所有的属性被初始化后调用。
 */
@Component //交给spring容器管理
public class ConstantPropertiesUtil implements InitializingBean {
    @Value("${aliyun.vod.file.keyid}")
    String accessKeyId;
    @Value("${aliyun.vod.file.keysecret}")
    String accessKeySecret;

    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;


    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = this.accessKeyId;
        ACCESS_KEY_SECRET = this.accessKeySecret;
    }
}
