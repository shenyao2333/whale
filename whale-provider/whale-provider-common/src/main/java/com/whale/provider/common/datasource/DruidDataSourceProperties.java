package com.whale.provider.common.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties("spring.datasource.druid")
public class DruidDataSourceProperties {
	private String username;
	private String password;
	private String jdbcUrl;
	private String driverClassName;
}
