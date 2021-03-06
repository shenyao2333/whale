package com.whale.api.order.oauth2.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sy
 * @date: 2021/3/10 17:24
 * @description
 */
public class WhaleUser2 {

    @Getter
    @Setter
    private String realName;


    @Getter
    @Setter
    private String avatar;


    @Getter
    @Setter
    private Integer userId;
}
