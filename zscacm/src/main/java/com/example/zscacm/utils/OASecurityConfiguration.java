package com.example.zscacm.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Slf4j
public class OASecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 开启允许跨域访问
        http.cors();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许跨域访问的站点
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        //允许跨域访问的methods
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST"));
        // 允许携带凭证
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //对所有URL生效
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }
}