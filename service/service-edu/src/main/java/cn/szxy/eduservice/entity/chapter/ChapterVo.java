package cn.szxy.eduservice.entity.chapter;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程章节实体类
 */
@ApiModel(value = "章节信息")
@Data
public class ChapterVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; //章节id
    private String title; //章节标题
    private List<VideoVo> children = new ArrayList<>();
}
