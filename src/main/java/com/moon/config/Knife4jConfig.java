package com.moon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Knife4j配置
 *
 * @author:Y.0
 * @date:2023/9/21
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Bean
    public Docket createRestApi1() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("博客开发接口文档")
                .description("基于SpringBoot + Vue开发的博客项目")
                .termsOfServiceUrl("localhost")
                .contact(new Contact("Y.0", "https://github.com/y962464", "424105202@qq.com"))
                .version("2.0")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("博客前台管理接口")
                .apiInfo(apiInfo)
                .select()
                //指定生成接口需要扫描指定的包名
                .apis(RequestHandlerSelectors.basePackage("com.moon.controller"))
                // 扫描所有有注解的api，用这种方式更灵活
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

//    @Bean
//    public Docket createRestApi2() {
//        ApiInfo apiInfo = new ApiInfoBuilder()
//                .title("博客开发接口文档")
//                .description("基于SpringBoot + Vue开发的博客项目")
//                .termsOfServiceUrl("localhost")
//                .contact(new Contact("Y.0", "https://github.com/y962464", "424105202@qq.com"))
//                .version("2.0")
//                .build();
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .groupName("博客后台管理接口")
//                .apiInfo(apiInfo)
//                .select()
//                //指定生成接口需要扫描指定的包名
//                .apis(RequestHandlerSelectors.basePackage("com.moon.controller.backstage"))
//                .paths(PathSelectors.any())
//                .build();
//        return docket;
//    }
}
