package com.whale.business.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale.business.system.domain.Shortcut;
import com.whale.business.system.service.ShortcutService;
import com.whale.provider.basices.web.R;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * @author entfrm
 * @date 2019-08-25 22:56:58
 * @description 快捷方式Controller
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/shortcut")
public class ShortcutController {

    private final ShortcutService shortcutService;

    private QueryWrapper<Shortcut> getQueryWrapper(Shortcut shortcut) {
        return new QueryWrapper<Shortcut>().like(StrUtil.isNotBlank(shortcut.getName()), "name", shortcut.getName()).eq(StrUtil.isNotBlank(shortcut.getRegion()), "region", shortcut.getRegion())
                .between(StrUtil.isNotBlank(shortcut.getBeginTime()) && StrUtil.isNotBlank(shortcut.getEndTime()), "create_time", shortcut.getBeginTime(), shortcut.getEndTime()).orderByAsc("sort");
    }

    @PreAuthorize("@ps.hasPerm('shortcut_view')")
    @GetMapping("/list")
    public R list(Page page, Shortcut shortcut) {
        IPage<Shortcut> shortcutPage = shortcutService.page(page, getQueryWrapper(shortcut));
        return R.ok(shortcutPage.getRecords(), shortcutPage.getTotal());
    }

    @PreAuthorize("@ps.hasPerm('shortcut_view')")
    @GetMapping("/shortcutList")
    public R shortcutList(Shortcut shortcut) {
        List<Shortcut> shortcutList = shortcutService.list(getQueryWrapper(shortcut));
        return R.ok(shortcutList);
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(shortcutService.getById(id));
    }


    @PreAuthorize("@ps.hasPerm('shortcut_add')")
    @PostMapping("/save")
    public R save(@RequestBody Shortcut shortcut) {
        shortcutService.saveOrUpdate(shortcut);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('shortcut_edit')")
    @PutMapping("/update")
    public R update(@RequestBody Shortcut shortcut) {
        shortcutService.updateById(shortcut);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('shortcut_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(shortcutService.removeByIds(Arrays.asList(id)));
    }
}
