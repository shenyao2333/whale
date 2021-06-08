package com.whale.business.system.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale.business.system.domain.Role;
import com.whale.business.system.domain.User;
import com.whale.business.system.domain.UserRole;
import com.whale.business.system.service.RoleService;
import com.whale.business.system.service.UserRoleService;
import com.whale.business.system.service.UserService;
import com.whale.business.system.vo.ResultVo;
import com.whale.business.system.vo.UserVo;
import com.whale.provider.basices.web.R;
import com.whale.provider.security.domain.WhaleUser;
import com.whale.provider.security.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/system/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    private QueryWrapper<User> getQueryWrapper(User user) {
        return new QueryWrapper<User>().like(StrUtil.isNotBlank(user.getUserName()), "user_name", user.getUserName()).like(StrUtil.isNotBlank(user.getNickName()), "nick_name", user.getNickName()).eq(StrUtil.isNotBlank(user.getStatus()), "status", user.getStatus())
                .eq(ObjectUtil.isNotNull(user.getDeptId()), "dept_id", user.getDeptId())
                .between(StrUtil.isNotBlank(user.getBeginTime()) && StrUtil.isNotBlank(user.getEndTime()), "create_time", user.getBeginTime(), user.getEndTime()).apply(StrUtil.isNotBlank(user.getSqlFilter()), user.getSqlFilter());
    }

    @PreAuthorize("@ps.hasPerm('user_view')")
    @GetMapping("/list")
    public R list(Page page, User user) {
        IPage<User> userIPage = userService.page(page, getQueryWrapper(user));
        return R.ok(userIPage.getRecords(), userIPage.getTotal());
    }

    @PreAuthorize("@ps.hasPerm('user_view')")
    @GetMapping("/userList")
    public R userList(User user) {
        List<UserVo> userList = userService.list(getQueryWrapper(user)).stream().map(userInfo -> {
            UserVo userVo = new UserVo();
            userVo.setId(userInfo.getId());
            userVo.setNickName(userInfo.getNickName());
            return userVo;
        }).collect(Collectors.toList());
        return R.ok(userList);
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        List<Integer> roles = new ArrayList<>();
        List<Role> roleList = roleService.list();
        if (user != null) {
            roles = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", user.getId()))
                    .stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList());

            user.setRoles(ArrayUtil.toArray(roles, Integer.class));
        }
        return R.ok(ResultVo.builder().result(user).extend(roleList).build());
    }

    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @GetMapping("/info")
    public R info() {
        User user = userService.getOne(Wrappers.<User>query()
                .lambda().eq(User::getUserName, SecurityUtil.getUser().getUsername()));
        if (user == null) {
            return R.fail("获取当前用户信息失败");
        }

        List<String> roles = SecurityUtil.getRoleIdList()
                .stream().map(roleId -> "ROLE_" + roleId).collect(Collectors.toList());
        WhaleUser whaleUser = SecurityUtil.getUser();
        user.setRoleList(whaleUser.getRoleIds());
        user.setPermissions(SecurityUtil.getPermission());
        return R.ok(user);
    }


    @PreAuthorize("@ps.hasPerm('user_add')")
    @PostMapping("/save")
    public R save(@RequestBody User user) {
        if (!StrUtil.isEmptyIfStr(user.getId()) && User.isAdmin(user.getId())) {
            return R.fail("不允许修改超级管理员");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('user_edit')")
    @PutMapping("/update")
    public R update(@RequestBody User user) {
        userService.saveUser(user);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('user_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable Integer[] id) {
        if (ArrayUtil.contains(id, 1)) {
            return R.fail("不允许删除超级管理员");
        }
        userService.removeByIds(Arrays.asList(id));
        return R.ok();
    }

    @GetMapping("/profile")
    public R profile() {
        WhaleUser whaleUser = SecurityUtil.getUser();
        if (whaleUser != null) {
            User user = userService.getById(whaleUser.getUserId() + "");
            if (user != null) {
                String roleNames = SecurityUtil.getRoleIdList().stream().map(roleId -> roleService.getById(roleId + "").getName())
                        .collect(Collectors.joining(","));
                user.setRoleNames(roleNames);
                user.setPassword(null);
            }
            return R.ok(user);
        } else {
            return R.fail("登录信息已过期，请重新登录");
        }
    }

    @PreAuthorize("@ps.hasPerm('user_edit')")
    @PutMapping("/updateProfile")
    public R updateProfile(@RequestBody User user) {
        userService.update(new UpdateWrapper<User>().eq("id", user.getId()).set("nick_name", user.getNickName()).set(StrUtil.isNotBlank(user.getPhone()), "phone", user.getPhone()).set("email", user.getEmail()).set("sex", user.getSex()));
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('user_edit')")
    @PutMapping("/updateAvatar")
    public R updateAvatar(@RequestParam("avatarfile") MultipartFile file, HttpServletRequest request) {
        //String avatar = "/profile/avatar/" + UploadUtil.fileUp(file, GlobalConfig.getAvatarPath(), "avatar" + new Date().getTime());
       // userService.update(new UpdateWrapper<User>().eq("id", SecurityUtil.getUser().getId()).set("avatar", avatar));
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('user_edit')")
    @PutMapping("/updatePwd")
    public R updatePwd(User user) {
        User user1 = userService.getById(SecurityUtil.getUser().getUserId());
        if (user1 != null && passwordEncoder.matches(user.getPassword(), user1.getPassword())) {
            userService.update(new UpdateWrapper<User>().eq("id", user1.getId()).set("password", passwordEncoder.encode(user.getNewPassword())));
            return R.ok();
        } else {
            return R.fail("原密码有误，请重试");
        }
    }


    @PreAuthorize("@ps.hasPerm('user_reset')")
    @PutMapping("/resetPwd")
    public R resetPwd(@RequestBody User user) {
        userService.update(new UpdateWrapper<User>().eq("id", user.getId()).set("password", passwordEncoder.encode(user.getPassword())));
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('user_edit')")
    @PutMapping("/changeStatus")
    public R changeStatus(@RequestBody User user) {
        if (User.isAdmin(user.getId())) {
            return R.fail("不允许修改超级管理员用户");
        }
        userService.update(new UpdateWrapper<User>().eq("id", user.getId()).set("status", user.getStatus()));
        return R.ok();
    }



    @PreAuthorize("@ps.hasPerm('user_export')")
    @GetMapping("/exportUser")
    public R exportUser(User user) {
     //   List<User> list = userService.list(getQueryWrapper(user));
     //   ExcelUtil<User> util = new ExcelUtil<User>(User.class);
     //   return util.exportExcel(list, "用户数据");
        return null;
    }

    @SneakyThrows

    @PreAuthorize("@ps.hasPerm('user_import')")
    @PostMapping("/importUser")
    public R importUser(MultipartFile file, boolean updateSupport) {
       //  ExcelUtil<User> util = new ExcelUtil<User>(User.class);
       // List<User> userList = util.importExcel(file.getInputStream());
       // String message = userService.importUser(userList, updateSupport);
        return R.ok();
    }

    @GetMapping("/importTemplate")
    public R importTemplate() {
      //  ExcelUtil<User> util = new ExcelUtil<User>(User.class);
      //  return util.importTemplateExcel("用户数据");
        return null;
    }
}
