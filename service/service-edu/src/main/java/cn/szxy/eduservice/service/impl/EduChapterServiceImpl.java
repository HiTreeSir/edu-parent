package cn.szxy.eduservice.service.impl;

import cn.szxy.eduservice.entity.EduChapter;
import cn.szxy.eduservice.entity.EduVideo;
import cn.szxy.eduservice.entity.chapter.ChapterVo;
import cn.szxy.eduservice.entity.chapter.VideoVo;
import cn.szxy.eduservice.mapper.EduChapterMapper;
import cn.szxy.eduservice.service.EduChapterService;
import cn.szxy.eduservice.service.EduVideoService;
import cn.szxy.servicebase.exception.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-08-24
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService videoService;
    /**
     * 查询并返回所有章节和小节信息
     * @param courseId
     * @return
     */
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //定义最终返回的实体类数据
        List<ChapterVo> chapterVoArrayList = new ArrayList<ChapterVo>();

        //通过课程id进行章节查询
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_Id",courseId);
        eduChapterQueryWrapper.orderByAsc("sort", "id");
        List<EduChapter> eduChapterList = baseMapper.selectList(eduChapterQueryWrapper);

        //通过课程id查询所有小节信息
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_Id",courseId);
        eduVideoQueryWrapper.orderByAsc("sort", "id");
        List<EduVideo> eduVideosList = videoService.list(eduVideoQueryWrapper);

        //填充vo章节信息
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);

            //创建章节对象
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            //将数据添加到最终返回中
            chapterVoArrayList.add(chapterVo);

            //填充课时vo数据
            //创建课程小节对象
            ArrayList<VideoVo> videoVos = new ArrayList<>();

            //循环遍历list中的小节
            for (int j = 0; j < eduVideosList.size(); j++) {

                EduVideo eduVideo = eduVideosList.get(j);
                if(eduChapter.getId().equals(eduVideo.getChapterId())){
                    //创建课时vo对象
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);

                    //对小节免费标识进行赋值
                    if(eduVideo.getIsFree()){
                        videoVo.setFree(true);
                    }else {
                        videoVo.setFree(false);
                    }

                    videoVos.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVos);
        }
        return chapterVoArrayList;
    }

    /**
     * 根据课程删除章节信息
     * @param chapterId
     * @return
     */
    @Override
    public boolean deleteChapterById(String chapterId) {
        //根据chapterId章节id查询小节表 ，如果查询到则不进行删除，
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",chapterId);
        int count = videoService.count(queryWrapper);
        if(count > 0){
            //查询出小节不进行删除
            throw new CustomException(20001,"清先删除小节信息");
        }else {
            //没有查询出小节信息，进行删除
            int i = baseMapper.deleteById(chapterId);
            return i > 0;
        }
    }

    /**
     * 根据课程id删除章节信息
     * @param id
     */
    @Override
    public void removeChapterByCourseId(String id) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",id);
        baseMapper.delete(wrapper);
    }
}
