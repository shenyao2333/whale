package com.whale.business.system.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale.business.system.domain.Application;
import com.whale.business.system.service.ApplicationService;
import com.whale.provider.basices.web.R;
import com.whale.provider.security.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/system/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    private QueryWrapper<Application> getQueryWrapper(Application application) {
        return new QueryWrapper<Application>()
                .like(StrUtil.isNotBlank(application.getName()), "name", application.getName())
                .eq(StrUtil.isNotBlank(application.getType()), "type", application.getType())
                .eq(StrUtil.isNotBlank(application.getStatus()), "status", application.getStatus())
                .orderByDesc("create_time");
    }

    //@PreAuthorize("@ps.hasPerm('application_view')")
    @GetMapping("/list")
    public R list(Page page, Application application) {
        IPage<Application> applicationPage = applicationService.page(page, getQueryWrapper(application));
        return R.ok(applicationPage.getRecords(), applicationPage.getTotal());
    }

    @GetMapping("/tree")
    public R tree( Application application) {
        List<Application> applicationList = applicationService.list(getQueryWrapper(application));
        return R.ok(applicationList);
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(applicationService.getById(id));
    }


    @PreAuthorize("@ps.hasPerm('application_add')")
    @PostMapping("/save")
    public R save(@Validated @RequestBody Application application) {
        application.setDeptId(SecurityUtil.getUser().getDeptId());
        applicationService.save(application);
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('application_edit')")
    @PutMapping("/update")
    public R update(@Validated @RequestBody Application application) {
        application.setDeptId(SecurityUtil.getUser().getDeptId());
        applicationService.updateById(application);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('application_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        if(ArrayUtil.contains(id, 1)){
            return R.fail("该应用不可删除");
        }
        return R.ok(applicationService.removeByIds(Arrays.asList(id)));
    }


    //@PreAuthorize("@ps.hasPerm('application_export')")
    //@GetMapping("/export")
    //public R export(Application application) {
    //    List<Application> list = applicationService.list(getQueryWrapper(application));
    //    ExcelUtil<Application> util = new ExcelUtil<Application>(Application.class);
    //    return util.exportExcel(list, "应用数据");
    //}
}
