package com.spm.resqjeevanredis.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info().title("ResQJeevan for Disaster Management")
                        .description("This is Disaster Management System developed by Team Tri-Safety Sentinels lead by Swayam Prakash Mohanty in Smart India Hackathon Grand Finale 2023")
                        .version("v2.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Certificate")
                        .url("https://drive.google.com/ﬁle/d/1uFr4qTtSK_bpykZwsQ3VHeoqEGN6iQ_/view?usp=sharing"));
    }
}
