package com.whale.business.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale.business.system.domain.Datasource;
import com.whale.business.system.service.DatasourceService;
import com.whale.provider.basices.web.R;
import com.whale.provider.dynamic.data.config.DynamicDataSourceConfig;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * @author entfrm
 * @date 2019-10-11 09:13:04
 * @description 数据库Controller
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/datasource")
public class DatasourceController {

    private final DatasourceService datasourceService;
    private final DynamicDataSourceConfig dynamicDataSourceConfig;

    private QueryWrapper<Datasource> getQueryWrapper(Datasource datasource) {
        return new QueryWrapper<Datasource>().orderByDesc("create_time");
    }

    @PreAuthorize("@ps.hasPerm('datasource_view')")
    @GetMapping("/list")
    @ResponseBody
    public R list(Page page, Datasource datasource) {
        IPage<Datasource> datasourcePage = datasourceService.page(page, getQueryWrapper(datasource));
        return R.ok(datasourcePage.getRecords(), datasourcePage.getTotal());
    }

    @PreAuthorize("@ps.hasPerm('datasource_view')")
    @GetMapping("/datasourceList")
    @ResponseBody
    public R datasourceList(Datasource datasource) {
        List<Datasource> datasourceList = datasourceService.list();
        return R.ok(datasourceList);
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(datasourceService.getById(id));
    }


    @PreAuthorize("@ps.hasPerm('datasource_add')")
    @PostMapping("/save")
    @ResponseBody
    public R save(@RequestBody Datasource datasource) {
        datasourceService.save(datasource);
        //保存id后更新 别名
        //datasource.setAlias(AliasUtil.genAlias(datasource.getDriverClassName(), datasource.getName(), datasource.getId()));
        datasourceService.updateById(datasource);
        dynamicDataSourceConfig.reload();
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('datasource_edit')")
    @PutMapping("/update")
    @ResponseBody
    public R update(@RequestBody Datasource datasource) {
        //datasource.setAlias(AliasUtil.genAlias(datasource.getDriverClassName(), datasource.getName(), datasource.getId()));
        datasourceService.updateById(datasource);
        dynamicDataSourceConfig.reload();
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('datasource_del')")
    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public R remove(@PathVariable("id") Integer[] id) {
        datasourceService.removeByIds(Arrays.asList(id));
        dynamicDataSourceConfig.reload();
        return R.ok();
    }
}
