package cn.szxy.vod.controller;

import cn.szxy.commonutils.R;
import cn.szxy.servicebase.exception.CustomException;
import cn.szxy.vod.service.VodService;
import cn.szxy.vod.utils.AliyunVodSDKUtils;
import cn.szxy.vod.utils.ConstantPropertiesUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Api(description="阿里云视频上传")
//@CrossOrigin //解决跨域问题
@RestController
@RequestMapping("/eduvod/video")
public class VodController {

    @Autowired
    private VodService vodService;

    /***
     *上传文件，并且返回id
     * @param file
     * @return
     */
    @ApiOperation("上传视频到阿里云")
    @PostMapping("uploadAlyVideo")
    public R uploadAlyVideo(MultipartFile file){
        //返回上传视频id
        String videoId = vodService.uploadAlyVideo(file);
        return R.ok().data("videoId",videoId);
    }

    @ApiOperation("根据id删除上传到阿里云的视频")
    @DeleteMapping("removeVideoById/{videoId}")
    public R removeVideoById(
            @ApiParam(name = "videoId", value = "云端视频id", required = true)
            @PathVariable String videoId){
        vodService.removeVideoById(videoId);
        return R.ok();
    }

    /**
     * 批量删除阿里云视频
     * @param videoIdList
     * @return
     */
    @ApiOperation("批量删除多个阿里云视频")
    @DeleteMapping("removeMoreVideoByIds")
    public R removeMoreVideoByIds(
            @ApiParam(name = "videoIdList", value = "云端视频id集合", required = true)
            @RequestParam("videoIdList") List videoIdList){
        vodService.removeMoreVideoByIds(videoIdList);
        return R.ok().message("视频删除成功");
    }

    @ApiOperation("根据视频云端id获取播放凭证")
    @GetMapping("getVideoPlayAuth/{videoId}")
    public R getVideoPlayAuth(@PathVariable String videoId){
        try {
            //初始化创建对象
            DefaultAcsClient client =
                    AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建获取凭证的request和response
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //设置视频id
            request.setVideoId(videoId);

            //调用方法得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            //获取播放凭证
            String playAuth = response.getPlayAuth();

            return R.ok().data("playAuth",playAuth);
        }catch(Exception e){
            throw new CustomException(20001,"获取阿里云视频播放凭证失败");
        }
    }

}
