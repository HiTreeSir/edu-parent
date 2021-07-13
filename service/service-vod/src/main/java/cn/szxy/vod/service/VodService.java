package cn.szxy.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    /**
     * 阿里云视频上传
     * @param file
     * @return 返回上传视频id
     */
    String uploadAlyVideo(MultipartFile file);


    /**
     * 通过id删除上传到阿里云视频中的id
     * @param videoId
     */
    void removeVideoById(String videoId);

    /**
     * 批量删除阿里云视频
     * @param videoIdList
     */
    void removeMoreVideoByIds(List videoIdList);
}
