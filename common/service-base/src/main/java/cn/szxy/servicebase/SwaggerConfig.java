package cn.szxy.servicebase;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 一、Swagger2介绍
 * 前后端分离开发模式中，api文档是最好的沟通方式。
 * Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。
 * 及时性 (接口变更后，能够及时准确地通知相关前后端开发人员)
 * 规范性 (并且保证接口的规范性，如接口的地址，请求方式，参数及响应格式和错误信息)
 * 一致性 (接口信息一致，不会出现因开发人员拿到的文档版本不一致，而出现分歧)
 * 可测性 (直接在接口文档上进行测试，以方便理解业务)
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                //.paths(Predicates.not(PathSelectors.regex("/admin/.*")))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();

    }

    private ApiInfo webApiInfo(){

        return new ApiInfoBuilder()
                .title("网站-课程中心API文档")
                .description("本文档描述了课程中心微服务接口定义")
                .version("1.0")
                .contact(new Contact("JackAndTom", "http://atguigu.com", "970200260@qq.com"))
                .build();
    }
}