package com.whale.oauth2.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 21:49
 * @description:
 */
@Data
@ApiModel
public class LoginDto implements Serializable {

    @NotNull(message = "用户名不能为空！")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @NotNull(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;


}
