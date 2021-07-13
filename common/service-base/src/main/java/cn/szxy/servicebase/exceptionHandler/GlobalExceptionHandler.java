package cn.szxy.servicebase.exceptionHandler;



import cn.szxy.commonutils.R;
import cn.szxy.servicebase.exception.CustomException;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //指定处理异常类型
    @ExceptionHandler(Exception.class)
    @ResponseBody //返回json格式数据
    public R errorException(Exception e){
        e.printStackTrace();
        return R.error().message("执行力全局异常处理");
    }

    //执行特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R arithmeticException(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行力ArithmeticException异常");
    }

    //自定义异常处理器
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public R customException(CustomException e){
        //将异常信息输入到日志文件当中
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
