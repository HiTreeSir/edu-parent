package cn.szxy.educms.controller;


import cn.szxy.commonutils.R;
import cn.szxy.educms.entity.CrmBanner;
import cn.szxy.educms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前台banner管理接口
 * </p>
 *
 * @author Jack
 * @since 2020-09-05
 */
@Api(description = "网站首页Banner列表")
@RestController
@RequestMapping("/educms/bannerFront")
//@CrossOrigin //解决跨域问题
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    //获取幻灯片
    @ApiOperation(value = "获取首页banner")
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> bannerList = bannerService.selectAllBanner();
        return R.ok().data("list",bannerList);
    }


}

