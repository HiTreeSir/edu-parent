package cn.szxy.eduservice.controller;


import cn.szxy.commonutils.R;
import cn.szxy.eduservice.entity.EduTeacher;
import cn.szxy.eduservice.entity.vo.TeacherQuery;
import cn.szxy.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-08-10
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
//@CrossOrigin //解决跨域问题
public class EduTeacherController {

    //service注入
    @Autowired
    private EduTeacherService eduTeacherService;

    /**
     * 查询所有讲师信息
     * @return
     */
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    /**
     * 根据id删除讲师信息(逻辑删除)
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@PathVariable String id){
        //删除成功返回ok格式信息，否则返回error格式信息
        return eduTeacherService.removeById(id)?R.ok():R.error().message("删除失败");

    }

    /**
     * 分页查询
     * @param current
     * @param limit
     * @return
     */
    @ApiOperation(value = "分页查询")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(
            @ApiParam(name = "current",value = "当前页码",required = true)
            @PathVariable Long current,
            @ApiParam(name = "limit",value = "每页显示记录数",required = true)
            @PathVariable Long limit){
        //创建分页工具类
        Page<EduTeacher> pageParam = new Page<>(current,limit);
        //调用service的方法自动封装查询信息到分页类teacherPage中
        eduTeacherService.page(pageParam, null);
        //用map集合存储返回信息
        Map<String,Object> pages = new HashMap<String,Object>();
        pages.put("total",pageParam.getTotal());//封装总记录数map集合中
        pages.put("rows",pageParam.getRecords());//封装所有查询信息进list中
        return R.ok().data(pages);
    }

    /**
     * 条件查询+分页功能
     * @param current
     * @param limit
     * @param teacherQuery
     * @return
     */
    @ApiOperation(value = "待条件的分页查询")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @ApiParam(name = "current",value = "当前页码",required = true)
            @PathVariable Long current,
            @ApiParam(name = "limit",value = "每页显示记录数",required = true)
            @PathVariable Long limit,
            @ApiParam(name = "teacherQuery", value = "查询对象", required = false)
            @RequestBody(required = false)
            TeacherQuery teacherQuery ){
        //创建分页工具类
        Page<EduTeacher> pageParam = new Page<>(current, limit);
        eduTeacherService.pageQuery(pageParam,teacherQuery);
        //用map集合存储返回信息
        Map<String,Object> pages = new HashMap<String,Object>();
        pages.put("total",pageParam.getTotal());//封装总记录数map集合中
        pages.put("rows",pageParam.getRecords());//封装所有查询信息进list中
        return R.ok().data(pages);
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("saveTeacher")
    public R saveTeacher(@ApiParam(name = "teacher",value = "讲师对象",required = true)
                         @RequestBody(required = false) EduTeacher teacher){

        return eduTeacherService.save(teacher)?R.ok():R.error();
    }

    /**
     * 根据id查询讲师信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询")
    @GetMapping("getTeacher/{id}")
    public R getById(@ApiParam(name = "id",value = "讲师id",required = true)
                      @PathVariable String id){
        //调用service对象的getById方法查询对象
        //模拟程序发生异常

        EduTeacher teacher = eduTeacherService.getById(id);
        return teacher!=null ? R.ok().data("teacher",teacher):R.error();
    }

    /**
     * 根据修改讲师信息
     * @param teacher
     * @return
     */
    @ApiOperation(value = "根据ID修改讲师")
    @PostMapping("updateTeacher")
    public R updateById(
                             @ApiParam(name = "teacher", value = "讲师对象", required = true)
                            @RequestBody EduTeacher teacher){
        eduTeacherService.updateById(teacher);
        return eduTeacherService.updateById(teacher)?R.ok():R.error();
    }
}

