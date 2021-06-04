package com.whale.business.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale.business.system.domain.LoginLog;
import com.whale.business.system.service.LoginLogService;
import com.whale.provider.basices.web.R;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 系统登录记录
 *
 * @author entfrm
 */
@RestController
@AllArgsConstructor
@RequestMapping("/monitor/loginLog")
public class LoginLogController {

    private final LoginLogService loginLogService;

    private QueryWrapper<LoginLog> getQueryWrapper(LoginLog loginLog) {
        return new QueryWrapper<LoginLog>().like(StrUtil.isNotBlank(loginLog.getLoginName()), "login_name", loginLog.getLoginName()).like(StrUtil.isNotBlank(loginLog.getLoginIp()), "login_ip", loginLog.getLoginIp())
                .eq(StrUtil.isNotBlank(loginLog.getStatus()), "status", loginLog.getStatus()).between(StrUtil.isNotBlank(loginLog.getBeginTime()) && StrUtil.isNotBlank(loginLog.getEndTime()), "login_time", loginLog.getBeginTime(), loginLog.getEndTime()).orderByDesc("id");
    }

    @PreAuthorize("@ps.hasPerm('loginLog_view')")
    @GetMapping("/list")
    public R list(Page page, LoginLog loginLog) {
        IPage<LoginLog> loginLogPage = loginLogService.page(page, getQueryWrapper(loginLog));
        return R.ok(loginLogPage.getRecords(), loginLogPage.getTotal());
    }


    @PreAuthorize("@ps.hasPerm('loginLog_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable Integer[] id) {
        try {
            loginLogService.removeByIds(Arrays.asList(id));
            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @PreAuthorize("@ps.hasPerm('loginLog_del')")
    @DeleteMapping("/clean")
    public R clean() {
        loginLogService.remove(new QueryWrapper<>());
        return R.ok();
    }


   // public R export(LoginLog loginLog) {
   //     List<LoginLog> list = loginLogService.list(getQueryWrapper(loginLog));
   //     ExcelUtil<LoginLog> util = new ExcelUtil<LoginLog>(LoginLog.class);
   //     return util.exportExcel(list, "登录日志" );
   // }
}
