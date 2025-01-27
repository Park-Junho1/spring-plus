package org.example.expert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // CSRF 보호 비활성화 (JWT 사용 시 필요)
            .authorizeRequests()
            .antMatchers("/auth/**").permitAll() // /auth/** 경로는 인증 없이 접근 허용
            .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
            .and()
            .addFilter(jwtFilter); // JWT 필터 추가

        return http.build();
    }

}
