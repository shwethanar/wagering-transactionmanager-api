package com.tabcorp.transactionmanagementapi.resource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@TestConfiguration
@EnableWebSecurity
public class SecurityTestConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable security for testing (not recommended for production)
        http
            .authorizeRequests()
            .antMatchers("/**")
            .permitAll()
            .and()
            .csrf().disable();
        
        
    }

    // Alternatively, configure security to work with a specific user
    // Example:
    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http
    //         .authorizeRequests()
    //         .antMatchers("/**")
    //         .authenticated()
    //         .and()
    //         .httpBasic();
    // }
    
    
}
