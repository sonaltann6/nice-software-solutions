package com.nss.simplexweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.nss.simplexweb.configuration.ProductServiceInterceptor;

@SuppressWarnings("deprecation")
@SpringBootApplication
@ComponentScan({"com.nss.simplexrest", "com.nss.simplexweb"})
public class SimplexWebApplication {
	
	@Autowired
	ProductServiceInterceptor productServiceInterceptor;

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
	         
	         @Override
	     	public void addInterceptors(InterceptorRegistry registry) {
	     	   registry.addInterceptor(productServiceInterceptor);
	     	}
	      };
	   }
}
