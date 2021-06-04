package com.whale.oauth2.mapper;

import com.whale.oauth2.domain.vo.AuthUserVo;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: sy
 * @Date: Created by 2021/6/4 16:38
 * @description: 用户登录
 */
public interface AuthUserMapper {


    AuthUserVo authUserByUserName(@Param("userName") String userName);


}
