package cn.szxy.eduservice.service.impl;

import cn.szxy.eduservice.entity.EduTeacher;
import cn.szxy.eduservice.entity.vo.TeacherQuery;
import cn.szxy.eduservice.mapper.EduTeacherMapper;
import cn.szxy.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-08-10
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery) {
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        //根据sort字段对数据进行排序
        //queryWrapper.orderByAsc("sort");

        //每页查询条件的或直接查询
        if(teacherQuery == null){
            baseMapper.selectPage(pageParam,queryWrapper);
        }

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断姓名是存在
        if(!StringUtils.isEmpty(name)){
            //模糊查询
            queryWrapper.like("name",name);
        }
        //构建条件
        //判断level是否为null
        if (!StringUtils.isEmpty(level)) {
            //等值比较
            queryWrapper.eq("level", level);
        }
        //构建条件
        if(!StringUtils.isEmpty(begin)){
            // >
            queryWrapper.ge("gmt_create",begin);
        }
        //构建条件
        if(!StringUtils.isEmpty(end)){
            // <
            queryWrapper.le("gmt_create",end);
        }
        //排序
        queryWrapper.orderByDesc("gmt_create");
        //执行分页查询
        baseMapper.selectPage(pageParam,queryWrapper);
    }

    /**
     * 查询热门讲师，存入redis缓存
     * @return
     */
    @Cacheable(value = "index",key = "'indexTeacherList'")
    @Override
    public List<EduTeacher> selectHotTeacher() {
        QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> eduTeachers = baseMapper.selectList(wrapperTeacher);
        return eduTeachers;
    }
    /**
     * 前台页面查询讲师信息
     * @param page
     * @return
     */
    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> page) {
        //用sort字段进行讲师排列，进行分页查询
        baseMapper.selectPage(page, new QueryWrapper<EduTeacher>().orderByDesc("sort"));

        //去除所有分页查询结果
        long total = page.getTotal(); //总记录数
        List<EduTeacher> records = page.getRecords(); //查询数据列表
        long pages = page.getPages(); //总页数
        long size = page.getSize(); //每页显示条数
        long current = page.getCurrent();  //当前页
        boolean hasNext = page.hasNext();  //是否具有下一页
        boolean hasPrevious = page.hasPrevious(); //是否具有上一页
        //封装到map集合中
        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("records",records);
        map.put("size",size);
        map.put("pages",pages);
        map.put("current",current);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);

        return map;
    }

    /**
     * 重写删除方法
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        Integer result = baseMapper.deleteById(id);
        return null != result && result>0;
    }
}
