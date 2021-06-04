package com.whale.provider.common.config;

import com.whale.provider.common.constant.DataSourceConstants;
import com.whale.provider.common.datasource.DruidDataSourceProperties;
import com.whale.provider.common.datasource.DynamicDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author entfrm
 * @date 2019-03-31
 *
 * 动态数据源切换配置
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class DynamicDataSourceConfig {
	private final Map<Object, Object> dataSourceMap = new HashMap<>(8);
	private final DruidDataSourceProperties dataSourceProperties;

	@Bean("dynamicDataSource")
	public DynamicDataSource dataSource() {
		DynamicDataSource ds = new DynamicDataSource();
		HikariDataSource dds = new HikariDataSource();
		dds.setJdbcUrl(dataSourceProperties.getUrl());
		dds.setDriverClassName(dataSourceProperties.getType());
		dds.setUsername(dataSourceProperties.getUsername());
		dds.setPassword(dataSourceProperties.getPassword());
		ds.setDefaultTargetDataSource(dds);
		dataSourceMap.put(0, dds);
		ds.setTargetDataSources(dataSourceMap);
		return ds;
	}

	/**
	 * 组装默认配置的数据源，查询数据库配置
	 */
	@PostConstruct
	public void init() {
		DriverManagerDataSource dds = new DriverManagerDataSource();
		dds.setUrl(dataSourceProperties.getUrl());
		dds.setDriverClassName(dataSourceProperties.getType());
		dds.setUsername(dataSourceProperties.getUsername());
		dds.setPassword(dataSourceProperties.getPassword());
		List<Map<String, Object>> dbList = new JdbcTemplate(dds).queryForList(DataSourceConstants.DS_QUERY_SQL);
		log.info("开始 -> 初始化动态数据源");
		Optional.of(dbList).ifPresent(list -> list.forEach(db -> {
			log.info("数据源:{}", db.get(DataSourceConstants.DS_NAME));
			HikariDataSource ds = new HikariDataSource();
			ds.setJdbcUrl(String.valueOf(db.get(DataSourceConstants.DS_JDBC_URL)));
			ds.setDriverClassName(String.valueOf(db.get(DataSourceConstants.DS_JDBC_DRIVER)));
			ds.setUsername((String) db.get(DataSourceConstants.DS_USER_NAME));

			String decPwd = (String) db.get(DataSourceConstants.DS_USER_PWD);
			ds.setPassword(decPwd);
			dataSourceMap.put(db.get(DataSourceConstants.DS_ALIAS), ds);
		}));

		log.info("完毕 -> 初始化动态数据源,共计 {} 条", dataSourceMap.size());
	}

	/**
	 * 重新加载数据源配置
	 */
	public Boolean reload() {
		init();
		DynamicDataSource dataSource = dataSource();
		dataSource.setTargetDataSources(dataSourceMap);
		dataSource.afterPropertiesSet();
		return Boolean.FALSE;
	}

}
