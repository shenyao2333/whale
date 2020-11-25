package com.whale.oauth2.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 21:49
 * @description:
 */
@Data
public class LoginDto implements Serializable {

    @NotNull(message = "用户名不能为空！")
    private String userName;

    @NotNull(message = "密码不能为空")
    private String password;


}
