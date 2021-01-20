package com.whale.provider.security.config;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


/**
 * 注入需要忽略的url
 * @author shenyao
 */

@Configuration
@ConfigurationProperties(prefix = "security.oauth2.client")
@RefreshScope
public class PermitProps {

	@Getter
	@Setter
	private List<String> ignoreUrls = new ArrayList<>();

}