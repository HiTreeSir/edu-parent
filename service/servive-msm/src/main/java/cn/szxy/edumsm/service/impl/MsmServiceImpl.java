package cn.szxy.edumsm.service.impl;

import cn.szxy.edumsm.service.MsmService;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAI4G5WRiCNuGsHoDP4D6UC";
    static final String accessKeySecret = "XvslqaKQVT0TdOgL3dMvI5chkX4lPR";

    /**
     * 短信发送
     * @param phone
     * @param param
     * @return
     */
    @Override
    public boolean sendSms(String phone, Map<String, Object> param)  {

            //判断接受短信的手机号是否为null
            if(StringUtils.isEmpty(phone)) return false;

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //设置固定参数
            CommonRequest request = new CommonRequest();
            //request.setProtocol(ProtocolType.HTTPS);
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");

            //设置发送的相关参数
            request.putQueryParameter("PhoneNumbers", phone);//手机号
            request.putQueryParameter("SignName", "程序猿在线教育网站");//签名名称
            request.putQueryParameter("TemplateCode", "SMS_201681667");//模板code
            //设置发送信息，将map集合转换成json格式
            request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));
        try {
            //最终发送
            CommonResponse response = acsClient.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}
