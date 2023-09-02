package com.tabcorp.transactionmanagementapi.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /* http
        .authorizeRequests()
            .antMatchers("/h2-console/**").permitAll() // Allow access to H2 Console
            .antMatchers("/transactions/**").hasAnyRole("ADMIN", "USER")
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/user/**").hasRole("USER")
            .antMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
        .logout()
            .permitAll();*/

    // Disable CSRF (Cross-Site Request Forgery) protection for H2 Console
    http.csrf().disable();
    http.headers().frameOptions().disable(); // Disable X-Frame-Options for H2 Console

    	 http.csrf().disable()
         .authorizeRequests()
         	.antMatchers("/h2-console/**").permitAll() // Allow access to H2 Console - remove in Production
             .antMatchers("/transactions/**").hasAnyRole("ADMIN", "USER")
             .anyRequest().authenticated()
         .and()
         .httpBasic();
        
    }

    //We define two users: "user" with the role "USER" and "admin" with the role "ADMIN."
    // Passwords are encoded using BCrypt for security.
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
