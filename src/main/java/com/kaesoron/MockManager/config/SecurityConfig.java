package com.kaesoron.MockManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

import static org.springframework.core.SpringProperties.getProperty;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        http
                .formLogin((formLogin) -> formLogin
                        .successHandler((request, response, authentication) -> {
                            // При успешной аутентификации перенаправляем на главную страницу
                            response.sendRedirect("/settings");
                        })
                );

        http.csrf().disable();
    }

    @Bean
    public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        configure(http);
        return http.build();
    }
}
