package cn.szxy.servicebase.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常类
 */
@Data
@AllArgsConstructor //生成属性的有参构造
@NoArgsConstructor //生成属性的无参构造
public class CustomException extends RuntimeException {
    @ApiModelProperty(value = "状态码")
    private Integer code;//状态码
    @ApiModelProperty(value = "异常信息")
    private String msg;//提示信息
}
