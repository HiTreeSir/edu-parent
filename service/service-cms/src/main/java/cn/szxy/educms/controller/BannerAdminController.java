package cn.szxy.educms.controller;


import cn.szxy.commonutils.R;
import cn.szxy.educms.client.OssClient;
import cn.szxy.educms.entity.CrmBanner;
import cn.szxy.educms.service.CrmBannerService;
import cn.szxy.servicebase.exception.CustomException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 后台banner管理接口
 * </p>
 *
 * @author Jack
 * @since 2020-09-05
 */
@Api(description = "后台banner管理接口")
@RestController
@RequestMapping("/educms/bannerAdmin")
//@CrossOrigin //解决跨域问题
public class BannerAdminController {
    @Autowired
    private CrmBannerService bannerService;
    @Autowired
    private OssClient ossClient;
    //分页查询
    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit){
        //创建分页工具类
        Page<CrmBanner> pageParam = new Page<CrmBanner>(page,limit);
        //调用service的方法自动封装查询信息到分页类teacherPage中
        bannerService.page(pageParam, null);
        //用map集合存储返回信息
        Map<String,Object> pages = new HashMap<String,Object>();
        pages.put("total",pageParam.getTotal());//封装总记录数map集合中
        pages.put("items",pageParam.getRecords());//封装所有查询信息进list中
        return R.ok().data(pages);
    }

    //添加
    @ApiOperation("添加幻灯片")
    @PostMapping("addBanner")
    public R addBeanner(@RequestBody CrmBanner crmBanner){
        bannerService.save(crmBanner);
        return R.ok();
    }

    //获取
    @ApiOperation("获取轮播图")
    @GetMapping("get/{id}")
    public R get(@PathVariable String id){
        CrmBanner crmBanner = bannerService.getById(id);
        return R.ok().data("item",crmBanner);
    }

    //删除
    /*@ApiOperation("删除轮播图")
    @DeleteMapping("deleteBanner/{id}")
    public R deleteBanner(@PathVariable String id){
        bannerService.removeBannerById(id);
        return R.ok();
    }*/

    @ApiOperation("删除轮播图")
    @DeleteMapping("deleteBanner/{id}")
    public R deleteBanner(@PathVariable String id){
        CrmBanner banner = bannerService.getById(id);
        String fileName = banner.getFileName();
        if(!StringUtils.isEmpty(fileName)){
            R r = ossClient.deleteFile(fileName);
            if (r.getCode() == 20001){
                throw new CustomException(20001,r.getMessage());
            }
        }
        boolean b = bannerService.removeById(id);
        return b?R.ok():R.error();
    }

    //修改
    @ApiOperation("修改轮播图")
    @PutMapping("updateBanner")
    public R updateBanner(@RequestBody CrmBanner banner){
        bannerService.updateById(banner);
        return R.ok();
    }
}

