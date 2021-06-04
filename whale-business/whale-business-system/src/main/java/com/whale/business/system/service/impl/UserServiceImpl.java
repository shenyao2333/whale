package com.whale.business.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale.business.system.domain.Dept;
import com.whale.business.system.domain.User;
import com.whale.business.system.domain.UserRole;
import com.whale.business.system.mapper.UserMapper;
import com.whale.business.system.service.ConfigService;
import com.whale.business.system.service.DeptService;
import com.whale.business.system.service.UserRoleService;
import com.whale.business.system.service.UserService;
import com.whale.provider.basices.web.GrabException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final ConfigService configService;
    private final UserRoleService userRoleService;
    private final DeptService deptService;


    @Override
    @Transactional
    public int saveUser(User user) {
        if (StrUtil.isEmptyIfStr(user.getId())) {
            // 新增用户信息
            int rows = baseMapper.insert(user);
            // 新增用户与角色管理
            addUserRole(user);
            return rows;
        } else {
            // 删除用户与角色关联
            userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", user.getId()));
            // 新增用户与角色管理
            addUserRole(user);
            user.setPassword(null);
            return baseMapper.updateById(user);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void addUserRole(User user) {
        Integer[] roles = user.getRoles();
        if (roles != null) {
            // 新增用户与角色管理
            for (Integer roleId : roles) {
                UserRole ur = new UserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                userRoleService.save(ur);
            }
        }
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    @Override
    public String importUser(List<User> userList, Boolean isUpdateSupport) {
        if (userList == null || userList.size() == 0) {
            throw new GrabException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.getValueByKey("user.initPassword");
        for (User user : userList) {
            try {
                // 验证是否存在这个用户
                User u = baseMapper.selectOne(new QueryWrapper<User>().eq("user_name", user.getUserName()));
                if (u == null) {
                    user.setPassword(new BCryptPasswordEncoder().encode(password));
                    if(StrUtil.isNotBlank(user.getDeptName())){
                        Dept dept = deptService.getOne(new QueryWrapper<Dept>().eq("name", user.getDeptName()));
                        if(dept != null){
                            user.setDeptId(dept.getDeptId());
                            user.setDeptName(dept.getName());
                        }
                    }
                    baseMapper.insert(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    this.updateById(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new GrabException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

}
