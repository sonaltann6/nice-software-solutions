package com.nss.simplexweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@SpringBootApplication
@ComponentScan({"com.nss.simplexrest", "com.nss.simplexweb"})
public class SimplexWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplexWebApplication.class, args);
	}
	
	 @Bean
	   public WebMvcConfigurer corsConfigurer() {
	      return new WebMvcConfigurerAdapter() {
	         @Override
	         public void addCorsMappings(CorsRegistry registry) {
	            registry.addMapping("/**")
	            .allowedOrigins("*")
	            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD");
	         }
	      };
	   }
}
