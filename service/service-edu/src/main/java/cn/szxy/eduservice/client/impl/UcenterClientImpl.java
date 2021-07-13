package cn.szxy.eduservice.client.impl;

import cn.szxy.commonutils.R;
import cn.szxy.eduservice.client.UcenterClient;
import cn.szxy.eduservice.entity.UcenterMember;
import cn.szxy.servicebase.exception.CustomException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
@Component
public class UcenterClientImpl implements UcenterClient {


    @Override
    public UcenterMember getInfo(String id) {
        throw new CustomException(20001,"查询会员信息失败");
    }
}
