package com.whale.provider.dynamic.data.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties("spring.datasource")
public class DataSourceProperties {
	private String username;
	private String password;
	private String url;
	private String type;
	private int minimumIdle;
	private int maximumPoolSize;
	private String poolName;
}
