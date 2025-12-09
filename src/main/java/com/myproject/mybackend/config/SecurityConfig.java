package com.myproject.mybackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //token을 쓰는 방식이므로 csrf를 disable
                .cors(cors -> {}) // ★ 반드시 추가
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests //HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
                        .requestMatchers("/api/user/**").permitAll() // 해당 주소에 대한 요청은 인증 없이 접근 허용.
                        .anyRequest().authenticated() //나머지 요청들에 대해서는 인증을 받야아 한다.
                );
        return http.build();
    }
}