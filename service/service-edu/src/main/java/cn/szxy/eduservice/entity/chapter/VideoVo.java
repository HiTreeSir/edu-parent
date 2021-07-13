package cn.szxy.eduservice.entity.chapter;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程小结信息
 */
@ApiModel(value = "课时信息")
@Data
public class VideoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; //课时信息id
    private String title; //课时信息标题
    private String videoSourceId; //云端视频id
    private Boolean free; //是否免费
}
