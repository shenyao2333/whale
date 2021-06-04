package com.whale.business.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.whale.business.system.domain.UserRole;
import com.whale.business.system.mapper.UserRoleMapper;
import com.whale.business.system.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author entfrm
 * @since 2019-01-30
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
