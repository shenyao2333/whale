package com.whale.provider.swagger.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author sy
 * @date: 2021/1/28 17:04
 * @description
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
               // .securitySchemes(Collections.singletonList(securitySchemes()))
               // .securityContexts(Collections.singletonList(securityContexts()));
    }


    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title("鲸鱼")
                // 描述
                .description("鲸鱼" + "接口文档")
                // 作者信息
                .contact(new Contact("鲸鱼", "http://www.sy91aa.com/", "sy91aa@163.com"))
                // 版本
                .version("版本号:0.0.1")
                .build();
    }



}
