package cn.szxy.eduservice.service.impl;

import cn.szxy.eduservice.entity.EduSubject;
import cn.szxy.eduservice.entity.excel.ExcelSubjectData;
import cn.szxy.eduservice.entity.subject.OneSubject;
import cn.szxy.eduservice.entity.subject.TwoSubject;
import cn.szxy.eduservice.listener.SubjectExcelListener;
import cn.szxy.eduservice.mapper.EduSubjectMapper;
import cn.szxy.eduservice.service.EduSubjectService;
import cn.szxy.servicebase.exception.CustomException;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.Subject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-08-18
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    /**
     * 添加课程分类
     * @param file
     */
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {
        try {
            //调用方法进行读取
            EasyExcel.read(file.getInputStream(), ExcelSubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(20002,"添加课程分类失败");
        }
    }

    /**
     * 返回所有一级分类和二级分类的课程数据
     * @return
     */
    @Override
    public List<OneSubject> getAllOneAndTwoSubject() {
        //最终得到的数据列表
        ArrayList<OneSubject> finalSubjectList = new ArrayList<>();

        //查询一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id",0);
        //进行查询
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //查询二级分类
        QueryWrapper<EduSubject> warpperTwo = new QueryWrapper<>();
        warpperTwo.ne("parent_id",0);
        List<EduSubject> twoSubjectList = baseMapper.selectList(warpperTwo);

        //将一级分类数据填充到 finalSubjectList中
        for (int i = 0; i < oneSubjectList.size(); i++) { //遍历一级分类
            //得到一级分类oneSubjectList的每一个eduSubject对象
            EduSubject eduSubject = oneSubjectList.get(i);
            //讲得到的每个eduSubject放到一级分类中去eduSubject
            OneSubject oneSubject = new OneSubject();
            //将eduSubject中的值复制到oneSubject中
            BeanUtils.copyProperties(eduSubject,oneSubject);
            //添加到最终类
            finalSubjectList.add(oneSubject);

            //将二级分类数据填充到 finalSubjectList中
            //在一级分类循环遍历中遍历所有二级分类
            //创建list集合封装每个一级分类的所有二级分类
            ArrayList<TwoSubject> finalTwoSubjectsList = new ArrayList<TwoSubject>();

            for (int j = 0; j < twoSubjectList.size(); j++) {
                //获取每个二级分类
                EduSubject tSubject = twoSubjectList.get(j);
                //判断改二级分类是否属于一级分类getParentId == getId
                if(tSubject.getParentId().equals(eduSubject.getId())){
                    //创建二级分类对象
                    TwoSubject twoSubject = new TwoSubject();
                    //将查询的到的二级分类拷贝到
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    //将二级分类添加到finalTwoSubjects中
                    finalTwoSubjectsList.add(twoSubject);
                }


            }
            //把一级下面的所有二级分类添加到一级分类中
            oneSubject.setChildren(finalTwoSubjectsList);
        }

        return finalSubjectList;
    }
}
