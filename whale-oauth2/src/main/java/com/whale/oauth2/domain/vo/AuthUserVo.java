package com.whale.oauth2.domain.vo;

import com.whale.oauth2.domain.SecurityUser;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.security.Security;
import java.util.Set;

/**
 * @Author: sy
 * @Date: Created by 2021/6/4 16:41
 * @description:
 */
@Data
public class AuthUserVo  implements Serializable {

    private String nickName;

    private String avatar;

    private Integer userId;

    private Integer deptId;

    private String userName;

    private String  password;

    private Set<Integer>  roleIds;

    private Set<String>  permsList;

}
