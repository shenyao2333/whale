package com.whale.business.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whale.business.system.domain.Area;
import com.whale.business.system.domain.Dept;
import com.whale.business.system.domain.RoleDept;
import com.whale.business.system.domain.User;
import com.whale.business.system.service.DeptService;
import com.whale.business.system.service.RoleDeptService;
import com.whale.business.system.service.UserService;
import com.whale.business.system.vo.ResultVo;
import com.whale.provider.basices.web.R;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 机构信息
 *
 * @author entfrm
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/dept")
public class DeptController {

    private final DeptService deptService;
    private final UserService userService;
    private final RoleDeptService roleDeptService;
    private final RedisTemplate redisTemplate;

    private QueryWrapper<Dept> getQueryWrapper(Dept dept) {
        return new QueryWrapper<Dept>().like(StrUtil.isNotBlank(dept.getName()), "name", dept.getName()).eq(StrUtil.isNotBlank(dept.getCode()), "code", dept.getCode())
                .between(StrUtil.isNotBlank(dept.getBeginTime()) && StrUtil.isNotBlank(dept.getEndTime()), "create_time", dept.getBeginTime(), dept.getEndTime());
    }

    @PreAuthorize("@ps.hasPerm('dept_view')")
    @GetMapping("/list")
    public R list(Dept dept) {
        List<Dept> deptList = deptService.list(getQueryWrapper(dept));
        if (deptList.size() > 0) {
            for (Dept dept1 : deptList) {
                if (StrUtil.isNotBlank(dept.getName()) || StrUtil.isNotBlank(dept.getCode())) {
                    dept1.setParentId(0);
                }
            }
        }
        return R.ok(deptList, deptList.size());
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(deptService.getById(id));
    }



    @PreAuthorize("@ps.hasPerm('dept_add')")
    @PostMapping("/save")
    public R save(@RequestBody Dept dept) {
        Dept info = null;
        if (0 != dept.getParentId()) {
            info = deptService.getById(dept.getParentId());
            dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        } else {
            dept.setAncestors("0");
        }
        deptService.save(dept);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('dept_edit')")
    @PutMapping("/update")
    public R update(@RequestBody Dept dept) {
        Dept info = null;
        if (0 != dept.getParentId()) {
            info = deptService.getById(dept.getParentId());
            dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        } else {
            dept.setAncestors("0");
        }
        deptService.updateById(dept);
        //更新用户机构名称
        new Thread() {
            public void run() {
                List<User> userList = userService.list(new QueryWrapper<User>().eq("dept_id", dept.getDeptId()));
                for (User user : userList) {
                    user.setDeptName(dept.getName());
                    userService.updateById(user);
                }
            }
        }.start();
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('dept_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer id) {
        if (deptService.getOne(new QueryWrapper<Dept>().eq("parent_id", id)) != null) {
            return R.fail("存在下级机构,不允许删除");
        }
        if (userService.getOne(new QueryWrapper<User>().eq("dept_id", id)) != null) {
            return R.fail("机构存在用户,不允许删除");
        }
        deptService.removeById(id);
        redisTemplate.delete("deptList");
        redisTemplate.delete("regionList");
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('dept_edit')")
    @GetMapping("/changeStatus")
    public R changeStatus(Dept dept) {
        deptService.updateById(dept);
        return R.ok();
    }

    /**
     * 加载机构列表树
     */
    @GetMapping("/deptTree")
    @ResponseBody
    public R deptTree() {
        List<Dept> deptList = deptService.list(new QueryWrapper<Dept>().orderByAsc("sort"));
        return R.ok(deptService.buildTree(deptList, 0));
    }

    /**
     * 加载角色机构（数据权限）列表树
     */
    @GetMapping("/roleDeptTree/{roleId}")
    @ResponseBody
    public R roleDeptTree(@PathVariable Integer roleId) {
        List<Dept> deptList = deptService.list(new QueryWrapper<Dept>().orderByAsc("sort"));
        List<Integer> depts = roleDeptService.list(new QueryWrapper<RoleDept>().eq("role_id", roleId))
                .stream().map(roleMenu -> roleMenu.getDeptId()).collect(Collectors.toList());
        return R.ok(ResultVo.builder().result(deptService.buildTree(deptList, 0)).extend(depts).build());
    }

    /**
     * 加载区域机构列表树
     */
    @GetMapping("/areaDeptTreeData")
    @ResponseBody
    public List<Map<String, Object>> areaTreeData(Area area) {
        List<Map<String, Object>> tree = deptService.areaDeptTreeData(area);
        return tree;
    }

}
