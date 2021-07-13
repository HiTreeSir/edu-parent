package cn.szxy.eduservice.controller;


import cn.szxy.commonutils.R;
import cn.szxy.eduservice.entity.EduChapter;
import cn.szxy.eduservice.entity.chapter.ChapterVo;
import cn.szxy.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
@Api(description="课程章节管理")
@RestController
@RequestMapping("/eduservice/chapter")
//@CrossOrigin //解决跨域问题
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;

    /**
     * 查询所有课程章节和小节
     * @param courseId
     * @return
     */
    @ApiOperation(value = "嵌套章节数据列表")
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId){
        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);

        return R.ok().data("allChapterVideo",list);
    }

    /**
     * 添加章节方法
     * @param eduChapter
     * @return
     */
    @ApiOperation(value = "章节添加")
    @PostMapping("addChapterInfo")
    public R addChapterInfo(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    @ApiOperation(value = "根据id查询章节")
    @GetMapping("getChapterById/{chapterId}")
    public R getChapterById(
            @ApiParam(name = "chapterId", value = "章节ID", required = true)
            @PathVariable String chapterId){
        EduChapter eduChapter = chapterService.getById(chapterId);

        return R.ok().data("chapter",eduChapter);
    }

    /**
     * 根据id修改章节
     * @param eduChapter
     * @return
     */
    @ApiOperation(value = "根据id修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
         chapterService.updateById(eduChapter);
         return R.ok();
    }

    /**
     * 根据章节id删除章节信息，如果改章节包含小节信息，则不能进行删除
     * @param chapterId
     * @return
     */
    @ApiOperation(value = "根据ID删除章节")
    @DeleteMapping("{chapterId}")
    public R deleteChapter(
            @ApiParam(name = "chapterId", value = "章节ID", required = true)
            @PathVariable String chapterId){
        boolean flag = chapterService.deleteChapterById(chapterId);

        if(flag){
            return R.ok();
        }else {
            return R.error();
        }

    }
}

