package cn.szxy.oss.controller;

import cn.szxy.commonutils.R;
import cn.szxy.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * oss文件上传与下载
 */
@Api(description="阿里云文件管理")
//@CrossOrigin //解决跨域问题
@RestController
@RequestMapping("/eduoss/edufile")

public class OssController {

    @Autowired
    private OssService ossService;

    /**
     * MultipartFile 改类是springmvc框架用来接受上传的文件的类
     * @param file
     * @return
     */
    //上传头像的方法
    @PostMapping("fileload")
    public R uploadOssFile(
            @ApiParam(name = "file",value = "文件",required = true)
            @RequestParam("file") MultipartFile file){
        //获取上传文件 MultipartFile
        //返回上传到oss的url路径
        String url = ossService.uploadFileAvatar(file);
        System.out.println(url);
        return R.ok().data("url",url);
    }
    @PostMapping("fileLoadPuls")
    public R uploadOssFileReturnFile(
            @ApiParam(name = "file",value = "文件",required = true)
            @RequestParam("file") MultipartFile file){
        //获取上传文件 MultipartFile
        //返回上传到oss的url路径
        List list = ossService.uploadFileAvatarReturnFileName(file);
        System.out.println(list);
        return R.ok().data("fileName",list.get(0)).data("url",list.get(1));
    }

    @DeleteMapping("deleteFile")
    public R deleteFile(@RequestParam("fileName") String fileName){
        System.out.println("deleteFile----------->" + fileName);
        ossService.deleteFileAvater(fileName);
        return R.ok();
    }

    @ApiOperation("根据id删除上传到阿里云的轮播图")
    @DeleteMapping("removeBannerById")
    public R removeBannerById(
            @ApiParam(name = "fileName", value = "fileName", required = true)
             @RequestBody  String fileName){
        System.out.println(fileName);
        ossService.deleteFileAvater(fileName);
        return R.ok();
    }
}
