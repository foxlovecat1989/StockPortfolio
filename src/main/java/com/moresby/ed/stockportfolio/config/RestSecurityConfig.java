package com.moresby.ed.stockportfolio.config;

import com.moresby.ed.stockportfolio.filter.JWTAuthenticationAndAuthorizationFilter;
import com.moresby.ed.stockportfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder  passwordEncoder;
//    @Autowired
//    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("matt").password("{noop}password").authorities("ROLE_ADMIN")
//                .and()
//                .withUser("jane").password("{noop}password").authorities("ROLE_USER");
//        // TODO: this password should be encoded
//
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);

        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/v1/basicAuth/**").permitAll()
                .antMatchers("/api/v1/basicAuth/**").hasAnyRole("ADMIN", "USER")
                .and()
                .httpBasic();
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                // .antMatchers(HttpMethod.GET, "/api/v1/activities/**").hasAnyRole("USER", "ADMIN")
                // .antMatchers("/api/v1/activities/**").hasRole("ADMIN")
                .antMatchers("/api/v1/**").permitAll()
                .and()
                .addFilter(new JWTAuthenticationAndAuthorizationFilter(authenticationManager()));

    }
}
