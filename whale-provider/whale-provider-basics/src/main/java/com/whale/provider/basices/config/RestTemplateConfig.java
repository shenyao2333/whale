package com.whale.provider.basices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * RestTemplate
 *
 * @author yong
 * @date 2018/8/16
 */
@Configuration
public class RestTemplateConfig {



/*
	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory factory){
		return new RestTemplate(factory);
	}
*/


	/**
	 * 请求非200的异常不抛出
	 * @return
	 */
	@Bean
	public RestTemplate restTemplate( ClientHttpRequestFactory factory){
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
		return restTemplate;
	}

	@Bean
	public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		//读取超时10秒,默认无限限制,单位：毫秒
		factory.setReadTimeout(100000);
		//连接超时时间,1分钟
		factory.setConnectTimeout(600000);
		return factory;
	}


	/**
	 * 解决微信返回json Content-Type 值却是 text/plain 的问题
	 */
	public class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
		public WxMappingJackson2HttpMessageConverter(){
			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.TEXT_PLAIN);
			mediaTypes.add(MediaType.TEXT_HTML);
			setSupportedMediaTypes(mediaTypes);
		}
	}
}
