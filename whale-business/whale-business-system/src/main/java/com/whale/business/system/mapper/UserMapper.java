package com.whale.business.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whale.business.system.domain.Dept;
import com.whale.business.system.domain.User;


/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author entfrm
 * @since 2019-01-30
 */
public interface UserMapper extends BaseMapper<User> {
    int addUser(User user);

    /**
     * 根据部门名称和部门类型查询用户信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public User selectUseByDept(Dept dept);
}
