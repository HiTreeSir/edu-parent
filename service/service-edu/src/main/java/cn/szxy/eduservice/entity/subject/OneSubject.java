package cn.szxy.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 一级分类实体类
 */
@Data
public class OneSubject {

    private String id; //课程id
    private String title; //课程标题
    private List<TwoSubject> children = new ArrayList<>(); //二级分类
}
