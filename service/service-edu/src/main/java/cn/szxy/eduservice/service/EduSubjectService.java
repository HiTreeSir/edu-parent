package cn.szxy.eduservice.service;

import cn.szxy.eduservice.entity.EduSubject;
import cn.szxy.eduservice.entity.subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-08-18
 */
public interface EduSubjectService extends IService<EduSubject> {
    /**
     * 添加课程分类
     * @param file
     */
    void saveSubject(MultipartFile file,EduSubjectService eduSubjectService);

    /**
     * 返回所有一级分类和二级分类的课程数据
     * @return
     */
    List<OneSubject> getAllOneAndTwoSubject();

}
