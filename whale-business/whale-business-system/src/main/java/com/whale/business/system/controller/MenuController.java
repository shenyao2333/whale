package com.whale.business.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whale.business.system.domain.Application;
import com.whale.business.system.domain.Menu;
import com.whale.business.system.domain.RoleMenu;
import com.whale.business.system.service.ApplicationService;
import com.whale.business.system.service.MenuService;
import com.whale.business.system.service.RoleMenuService;
import com.whale.business.system.service.UserService;
import com.whale.business.system.vo.ResultVo;
import com.whale.provider.basices.web.R;
import com.whale.provider.security.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单信息
 *
 * @author entfrm
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/menu")
public class MenuController {

    private final ApplicationService applicationService;
    private final MenuService menuService;
    private final RoleMenuService roleMenuService;
    private final UserService userService;

    private QueryWrapper<Menu> getQueryWrapper(Menu menu) {
        return new QueryWrapper<Menu>().like(StrUtil.isNotBlank(menu.getName()), "name", menu.getName()).eq(StrUtil.isNotBlank(menu.getStatus()), "status", menu.getStatus())
                .eq(menu.getApplicationId()!=null,"application_id",menu.getApplicationId())
                .orderByAsc("sort")
                .between(StrUtil.isNotBlank(menu.getBeginTime()) && StrUtil.isNotBlank(menu.getEndTime()), "create_time", menu.getBeginTime(), menu.getEndTime());
    }

    @PreAuthorize("@ps.hasPerm('menu_view')")
    @GetMapping("/list")
    public R list(Menu menu) {
        List<Menu> menuList = menuService.list(getQueryWrapper(menu));
        if (menuList.size() > 0) {
            for (Menu menu1 : menuList) {
                if (StrUtil.isNotBlank(menu.getName()) || StrUtil.isNotBlank(menu.getStatus())) {
                    menu1.setParentId(0);
                }
            }
        }
        return R.ok(menuList, menuList.size());
    }

    @GetMapping
    public R getMenus() {
        List<Application> applications = applicationService.list(new QueryWrapper<Application>().orderByAsc("sort"));
        Set<Menu> menuSet = new HashSet<>();
        SecurityUtil.getRoleIdList().forEach(roleId -> menuSet.addAll(menuService.selectMenuListByRoleId(roleId)));
        List<Menu> menuList = menuSet.stream().sorted(Comparator.comparingInt(Menu::getSort)).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("applications", applications);
        map.put("menus", menuService.buildMenus(menuService.buildTree(menuList, 0)));
        return R.ok(map);
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(menuService.getById(id));
    }


    @PreAuthorize("@ps.hasPerm('menu_add')")
    @PostMapping("/save")
    public R save(@RequestBody Menu menu) {
        menuService.save(menu);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('menu_edit')")
    @PutMapping("/update")
    public R update(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('menu_del')")
    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public R remove(@PathVariable("id") Integer id) {
        if (menuService.count(new QueryWrapper<Menu>().eq("parent_id", id)) > 0) {
            return R.fail("存在子菜单,不允许删除");
        }
        if (roleMenuService.count(new QueryWrapper<RoleMenu>().eq("menu_id", id)) > 0) {
            return R.fail("菜单已分配,不允许删除");
        }
        menuService.removeById(id);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('menu_edit')")
    @GetMapping("/changeStatus")
    public R changeStatus(Menu menu) {
        menuService.updateById(menu);
        return R.ok();
    }

    /**
     * 加载所有菜单列表树
     */
    @GetMapping("/menuTree")
    @ResponseBody
    public R menuTree(@RequestParam(required = false) String applicationIds) {
        List<Menu> menuList = menuService.list(new QueryWrapper<Menu>().in(StrUtil.isNotBlank(applicationIds), "application_id", applicationIds).eq("status", "0").orderByAsc("sort"));
        return R.ok(menuList);
    }

    /**
     * 加载角色菜单列表树
     */
    @GetMapping("/roleMenuTree/{roleId}")
    public R roleMenuTree(@PathVariable Integer roleId) {
        List<Menu> menuList = menuService.list(new QueryWrapper<Menu>().eq("status", "0").orderByAsc("sort"));
        return R.ok(ResultVo.builder().result(menuList).extend(menuService.selectMenusByRoleId(roleId)).build());
    }

}
