package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.service.ApiInfo.DEFAULT_CONTACT;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfo("REST API Basics", "Documentation REST API", "1.0", " ", "bogdan@gmail.com", " ", " "))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
