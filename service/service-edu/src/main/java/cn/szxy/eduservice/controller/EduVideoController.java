package cn.szxy.eduservice.controller;


import cn.szxy.commonutils.R;
import cn.szxy.eduservice.client.VodClient;
import cn.szxy.eduservice.entity.EduVideo;
import cn.szxy.eduservice.service.EduVideoService;
import cn.szxy.servicebase.exception.CustomException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
@Api(description = "课程小节管理")
@RestController
@RequestMapping("/eduservice/eduvideo")
//@CrossOrigin //解决跨域问题
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    /**
     * 添加小节
     * @param eduVideo
     * @return
     */
    @PostMapping("addVideoInfo")
    public R addVideoInfo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }

    /**
     * 删除小节信息，删除对应的阿里云视频
     * @param id
     * @return
     */
    //TODO: 后面这个方法需要完善，删除小节信息的时候需要连带删除视频信息
    @DeleteMapping("{id}")
    public R deleteVideoInfo(@PathVariable String id){
        //根据小节id获取，上传到阿里云的视频id，远程调用方法删除阿里云视频
        EduVideo eduVideo = videoService.getById(id);
        //视频id
        String videoSourceId = eduVideo.getVideoSourceId();
        //如果视频id不为null，调用远程id删除上传到阿里云的小节视频
        if(!StringUtils.isEmpty(videoSourceId)){
            R result = vodClient.removeVideoById(videoSourceId);
            //判断当前方法是否触发熔断器
            if(result.getCode() == 20001){
                throw new CustomException(20001,result.getMessage());
            }
        }
        //删除小节id
        boolean b = videoService.removeById(id);

        return b?R.ok():R.error();
    }

    /**
     * 修改小节信息
     * @param eduVideo
     * @return
     */
    @PostMapping("updateVideoInfo")
    public R updateVidemInfo(@RequestBody EduVideo eduVideo){
         videoService.updateById(eduVideo);
        return R.ok();
    }

    /**
     * 通过id查询小节信息
     * @param id
     * @return
     */
    @GetMapping("getVideoById/{id}")
    public R getVideoById(@PathVariable String id){



        EduVideo video = videoService.getById(id);
        return R.ok().data("videoInfo",video);
    }
}

