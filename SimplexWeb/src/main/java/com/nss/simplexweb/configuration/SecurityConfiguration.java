package com.nss.simplexweb.configuration;

import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	 
    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/noAuth/**").permitAll()	//For Country List in Registration Page
                .antMatchers("/login").permitAll()
                .antMatchers("/rest/**").permitAll()	//For REST API
                .antMatchers("/registration").permitAll()
                .antMatchers("/forgotPassword").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
                	.authenticated().and().csrf().disable()
                .formLogin()
	                .loginPage("/login")
	                .failureUrl("/login?error=true")
	                .defaultSuccessUrl("/user/home")
	                .usernameParameter("email")
	                .passwordParameter("password")
	                .successHandler(myAuthenticationSuccessHandler())
	                .and()
	            .logout()
	                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	                .logoutSuccessUrl("/")
	                .and()
	            .exceptionHandling()
                	.accessDeniedPage("/access-denied")
                	.and()
                .sessionManagement()
                	.invalidSessionUrl("/login");
    	
    	/*http
        .csrf().disable()
        .authorizeRequests()
        	.anyRequest()
        		.permitAll()
        	.and()
        	.httpBasic()
        		.disable()
        .sessionManagement().disable();*/
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
                			"/img/**", "/email_templates/**", "/font-awesome/**", "/fonts/**",
                			"/LESS/**", "/locales/**", "/pdf/**", "/favicon.ico");
        
        //For REST API
        web
        	.ignoring()
        		.antMatchers("/v2/api-docs/**", "/configuration/ui/**", "/swagger-resources/**",
        				"/configuration/security/**", "/swagger-ui.html/**", "/webjars/**", "/api/**");
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
    
    @Bean
    public HttpSessionListener httpSessionListener(){
        // MySessionListener should implement javax.servlet.http.HttpSessionListener
        return new SessionConfig(); 
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }
}
