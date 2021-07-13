package cn.szxy.orderservice.client;

import cn.szxy.commonutils.orderVo.CourseInfoVoOrder;
import cn.szxy.orderservice.client.impl.EduClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-edu")
public interface EduClient {

    @PostMapping("/eduservice/coursefront/getCourseInfoOrder/{id}")
    public CourseInfoVoOrder getCourseInfoOrder(@PathVariable("id") String id);

    @GetMapping("/eduservice/coursefront/updateBuyCount/{courseId}")
    public void updateBuyCount(@PathVariable("courseId") String courseId);
}
