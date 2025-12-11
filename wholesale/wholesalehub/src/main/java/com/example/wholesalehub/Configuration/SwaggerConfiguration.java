package com.example.wholesalehub.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {
	  @Bean
	    public OpenAPI customOpenAPI() {
	        return new OpenAPI()
	             .info(new Info()
	             .title("Wholesale Hub API"))
	             .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
		         .components(new Components()
		         .addSecuritySchemes("bearerAuth",
		          new SecurityScheme()
		          .type(SecurityScheme.Type.HTTP)
		          .scheme("bearer")
		          .bearerFormat("JWT")));
		    }	                             
	    }
