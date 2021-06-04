package com.whale.business.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whale.business.system.domain.User;

import java.util.List;


public interface UserService extends IService<User> {

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int saveUser(User user);

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    String importUser(List<User> userList, Boolean isUpdateSupport);

}
