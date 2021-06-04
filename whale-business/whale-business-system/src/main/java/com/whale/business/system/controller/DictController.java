package com.whale.business.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale.business.system.domain.Dict;
import com.whale.business.system.service.DictService;
import com.whale.provider.basices.web.R;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author entfrm
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/dict")
public class DictController {

    private final DictService dictService;

    private QueryWrapper<Dict> getQueryWrapper(Dict dict) {
        return new QueryWrapper<Dict>().like(StrUtil.isNotBlank(dict.getName()), "name", dict.getName()).orderByDesc("id")
                .eq(StrUtil.isNotBlank(dict.getType()), "type", dict.getType()).eq(StrUtil.isNotBlank(dict.getStatus()), "status", dict.getStatus());
    }

    @PreAuthorize("@ps.hasPerm('dict_view')")
    @GetMapping("/list")
    public R list(Page page, Dict dict) {
        IPage<Dict> dictPage = dictService.page(page, getQueryWrapper(dict));
        return R.ok(dictPage.getRecords(), dictPage.getTotal());
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(dictService.getById(id));
    }


    @PreAuthorize("@ps.hasPerm('dict_add')")
    @PostMapping("/save")
    @ResponseBody
    public R save(@RequestBody Dict dict) {
        dictService.save(dict);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('dict_edit')")
    @PutMapping("/update")
    @ResponseBody
    public R update(@RequestBody Dict dict) {
        dictService.updateById(dict);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('dict_del')")
    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public R remove(@PathVariable Integer[] id) {
        try {
            dictService.removeByIds(Arrays.asList(id));
            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }


    @PreAuthorize("@ps.hasPerm('dict_edit')")
    @GetMapping("/changeStatus")
    @ResponseBody
    public R changeStatus(Dict dict) {
        dictService.updateById(dict);
        return R.ok();
    }

    /**
     * 获取所有字典列表
     */
    @GetMapping("/dictList")
    public R dictList() {
        List<Dict> dictList = dictService.list();
        return R.ok(dictList);
    }
}
