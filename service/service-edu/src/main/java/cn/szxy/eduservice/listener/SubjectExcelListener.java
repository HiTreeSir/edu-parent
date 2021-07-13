package cn.szxy.eduservice.listener;

import cn.szxy.eduservice.entity.EduSubject;
import cn.szxy.eduservice.entity.excel.ExcelSubjectData;
import cn.szxy.eduservice.service.EduSubjectService;
import cn.szxy.servicebase.exception.CustomException;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Map;

/**
 * 改类不能交给spring容器管理，所以要通过构造方法注入来注入其他对象
 */
public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    public EduSubjectService eduSubjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    /**
     *该方法读取的表头数据
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }


    /**
     * 读取excel中的数据，一行一行读取
     * @param excelSubjectData
     * @param analysisContext
     */
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        //判断excel是否为空
        if(excelSubjectData == null){
            throw new CustomException(20001,"添加失败");
        }

        //添加一级分类
        //判断一级分类数据是否存在
        EduSubject existOneSubject = this.existOneSubject(eduSubjectService, excelSubjectData.getOneSubjectName());
        if(existOneSubject == null){
            //表中没有重复数据
            existOneSubject = new EduSubject();
            existOneSubject.setTitle(excelSubjectData.getOneSubjectName()); //一级分类名称
            existOneSubject.setParentId("0"); //一级分类编号
            boolean save = eduSubjectService.save(existOneSubject);

        }
        //获取一级分类的id纸
        String pid = existOneSubject.getId();
        //添加二级分类
        EduSubject existTwoSubject = this.existTwoSubject(eduSubjectService,excelSubjectData.getTwoSubjectName(),pid);
        if(existTwoSubject == null){
            existTwoSubject = new EduSubject();
            existTwoSubject.setTitle(excelSubjectData.getTwoSubjectName());
            existTwoSubject.setParentId(pid);
            eduSubjectService.save(existTwoSubject);
        }
    }

    //判断一级分类是否重复 select * from edu_subject where title = ?   and parent_id =0;
    private EduSubject existOneSubject(EduSubjectService eduSubjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject eduSubject = eduSubjectService.getOne(wrapper);
        System.out.println("一级分类：" + eduSubject);
        return eduSubject;
    }

    //判断二级分类是否重复 select * from edu_subject where title = ? and parent_id = ?;
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject eduSubject = eduSubjectService.getOne(wrapper);
        System.out.println("二级分类：" + eduSubject);
        return eduSubject;
    }
    /**
     * 读取完后进行操作
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
