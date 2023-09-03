package com.tabcorp.transactionmanagementapi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.SwaggerUiConfigParameters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springdoc.core.GroupedOpenApi;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springdoc.core.GroupedOpenApi;
//import org.springdoc.webmvc.api.OpenApiConfigProperties;
//import org.springdoc.webmvc.ui.SwaggerUiConfigParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("public-api")
            .pathsToMatch("/transactions/**")
            .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
            .group("admin-api")
            .pathsToMatch("/admin/**")
            .build();
    }

    /*@Bean
    @Primary
    public SwaggerUiConfigParameters swaggerUiConfigParameters() {
        SwaggerUiConfigParameters parameters = new SwaggerUiConfigParameters();
        parameters.setDefaultModelExpandDepth(1);
        parameters.setDefaultModelRendering(SwaggerUiConfigParameters.ModelRendering.EXAMPLE);
        parameters.setDefaultModelsExpandDepth(1);
        parameters.setDeepLinking(true);
        parameters.setDisplayOperationId(false);
        parameters.setDisplayRequestDuration(false);
        parameters.setFilter(true); // Use true to enable the filter
        parameters.setMaxDisplayedTags(null);
        parameters.setOperationsSorter(SwaggerUiConfigParameters.OperationsSorter.ALPHA);
        parameters.setShowExtensions(true);
        parameters.setTagsSorter(SwaggerUiConfigParameters.TagsSorter.ALPHA);
        
        // You can customize other parameters as needed
        
        return parameters;
    }

    @Bean
    public OpenApiConfigProperties openApiConfigProperties() {
        return new OpenApiConfigProperties();
    }*/
}
