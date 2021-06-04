package com.whale.business.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale.business.system.domain.ClientDetails;
import com.whale.business.system.service.ClientDetailsService;
import com.whale.provider.basices.web.R;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


/**
 * @author entfrm
 * @date 2019-10-11 09:13:04
 * @description 授权客户端Controller
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/clientDetails")
public class ClientDetailsController {

    private final ClientDetailsService clientDetailsService;
    private final PasswordEncoder passwordEncoder;

    private QueryWrapper<ClientDetails> getQueryWrapper(ClientDetails clientDetails) {
        return new QueryWrapper<ClientDetails>().like(StrUtil.isNotBlank(clientDetails.getClientId()), "client_id", clientDetails.getClientId());
    }

    @PreAuthorize("@ps.hasPerm('clientDetails_view')")
    @GetMapping("/list")
    @ResponseBody
    public R list(Page page, ClientDetails clientDetails) {
        IPage<ClientDetails> clientDetailsPage = clientDetailsService.page(page, getQueryWrapper(clientDetails));
        return R.ok(clientDetailsPage.getRecords(), clientDetailsPage.getTotal());
    }

    @GetMapping("/{clientId}")
    public R getById(@PathVariable("clientId") String clientId) {
        return R.ok(clientDetailsService.getById(clientId));
    }


    @PreAuthorize("@ps.hasPerm('clientDetails_add')")
    @PostMapping("/save")
    @ResponseBody
    public R save(@RequestBody ClientDetails clientDetails) {
        clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));
        clientDetailsService.save(clientDetails);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('clientDetails_edit')")
    @PutMapping("/update")
    @ResponseBody
    public R update(@RequestBody ClientDetails clientDetails) {
        clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));
        clientDetailsService.updateById(clientDetails);
        return R.ok();
    }


    @PreAuthorize("@ps.hasPerm('clientDetails_del')")
    @DeleteMapping("/remove/{clientId}")
    @ResponseBody
    public R remove(@PathVariable("clientId") String clientId) {
        return R.ok(clientDetailsService.removeById(clientId));
    }
}
