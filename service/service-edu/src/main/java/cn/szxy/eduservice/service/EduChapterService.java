package cn.szxy.eduservice.service;

import cn.szxy.eduservice.entity.EduChapter;
import cn.szxy.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
public interface EduChapterService extends IService<EduChapter> {
    /**
     * 查询所有课程章节和小节
     * @param courseId
     * @return
     */
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    /**
     * 根据id删除章节
     * @param chapterId
     * @return
     */
    boolean deleteChapterById(String chapterId);

    /**
     * 根据课程id删除章节信息
     * @param id
     */
    void removeChapterByCourseId(String id);
}
