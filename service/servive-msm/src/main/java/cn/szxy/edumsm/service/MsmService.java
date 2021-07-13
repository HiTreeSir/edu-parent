package cn.szxy.edumsm.service;

import com.aliyuncs.exceptions.ClientException;

import java.util.Map;

public interface MsmService {
    /**
     * 阿里云短信服务
     * @param phone
     * @param param
     * @return
     */
    boolean sendSms(String phone, Map<String, Object> param);
}
