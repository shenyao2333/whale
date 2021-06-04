package com.whale.oauth2.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.crypto.SecureUtil;
import com.whale.provider.basices.web.R;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 验证码
 *
 * @author maohua.lin
 * @create 2020-05-28 15:07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/captcha")
public class CaptchaController {


    private final RedisTemplate redisTemplate;

    /**
     * redis验证码目录
     **/
    private final String REDIS_DIR = "captcha:";

    /**
     * 宽
     */
    private final Integer WIDTH = 120;
    /**
     * 高
     */
    private final Integer HEIGHT = 40;
    /**
     * 编码长度
     */
    private final Integer CODE_COUNT = 4;
    /**
     * 干扰线数
     */
    private final Integer LINE_COUNT = 20;


    /**
     * 验证码
     *
     * @param key
     */
    @GetMapping(value = "/image/{key}")
    public R randomImage(@PathVariable String key) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(WIDTH, HEIGHT, CODE_COUNT, LINE_COUNT);
        String code = lineCaptcha.getCode();
        String realKey = SecureUtil.md5(code + key);
        redisTemplate.opsForValue().set(REDIS_DIR+realKey, code, 60, TimeUnit.SECONDS);
        Map<String, String> res = new HashMap<>(2);
        res.put("realKey", realKey);
        res.put("img", lineCaptcha.getImageBase64());
        return R.ok(res);
    }
}
