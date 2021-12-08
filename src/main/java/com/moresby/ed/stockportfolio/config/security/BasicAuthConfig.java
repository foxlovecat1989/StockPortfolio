package com.moresby.ed.stockportfolio.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class BasicAuthConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("matt").password("{noop}password").authorities("ROLE_ADMIN")
                .and()
                .withUser("jane").password("{noop}password").authorities("ROLE_USER");
        // TODO: this password should be encoded
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/v1/basicAuth/**").permitAll()
                .antMatchers("/api/v1/basicAuth/**").hasRole("ADMIN")
                .and()
                .httpBasic();
    }
}
