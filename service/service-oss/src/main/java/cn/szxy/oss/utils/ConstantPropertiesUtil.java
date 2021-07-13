package cn.szxy.oss.utils;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 常量类，读取配置文件application.properties中的配置
 *
 * 用spring的 InitializingBean接口 的 afterPropertiesSet() 来初始化配置信息，
 * 这个方法将在所有的属性被初始化后调用。
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {

    //读取配置文件内容

    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;  //访问节点

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;  //密钥id

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret; //密码

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;  //项目名称


    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    /**
     * 这个方法将在所有的属性被初始化后调用。
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = this.endpoint;
        ACCESS_KEY_ID = this.keyId;
        ACCESS_KEY_SECRET = this.keySecret;
        BUCKET_NAME = this.bucketName;
    }
}
