package com.whale.business.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/23 20:44
 * @description:
 */
@Data
@ApiModel
public class OssSTSVo implements Serializable {

    private static final long serialVersionUID = 6136004871361678295L;

    @ApiModelProperty(value = "密钥标识")
    private String  accessKey;

    @ApiModelProperty(value = "访问密钥")
    private String  accessKeySecret;

    @ApiModelProperty(value = "安全令牌。")
    private String  securityToken;

    @ApiModelProperty(value = "有效时间，单位秒")
    private Long expiration;
}
