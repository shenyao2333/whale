package com.whale.oauth2.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whale.oauth2.domain.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser> {

    List<SysUser> selectByUserName(@Param("userName")String userName);

    List<SysUser> selectByPhone(@Param("phone")String phone);



}
