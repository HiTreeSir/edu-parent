package cn.szxy.eduservice.controller;


import cn.szxy.commonutils.R;
import cn.szxy.eduservice.entity.subject.OneSubject;
import cn.szxy.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-08-18
 */
@Api(description = "课程分类管理")
@RestController
//@CrossOrigin //解决跨域问题

@RequestMapping("/eduservice/subject")
public class EduSubjectController {
    @Autowired
    private EduSubjectService eduSubjectService;

    /**
     * 添加课程分类
     * 获取上传过来的文件，将文件中的内容读取出来
     * @param file
     * @return
     */
    @ApiOperation(value = "上传课程分类文件")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        eduSubjectService.saveSubject(file,eduSubjectService);
        return R.ok();
    }

    /**
     * 查询所有课程分类列表
     * @return
     */
    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        //返回一级分类，因为一级分类包含二级分类的信息集合
        List<OneSubject> list = eduSubjectService.getAllOneAndTwoSubject();
        return R.ok().data("list",list);
    }
}

