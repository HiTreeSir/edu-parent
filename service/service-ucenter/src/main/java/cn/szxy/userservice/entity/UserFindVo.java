package cn.szxy.userservice.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(value="用户查找类", description="用户查找类")
public class UserFindVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "是否禁用")
    private Boolean isDisabled;
}
