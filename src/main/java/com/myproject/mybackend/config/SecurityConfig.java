package com.myproject.mybackend.config;

import com.myproject.mybackend.jwt.filter.JWTFilter;
import com.myproject.mybackend.jwt.filter.LoginFilter;
import com.myproject.mybackend.jwt.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //  session 방식
//    private final AuthSuccessHandler authSuccessHandler;
//
//    public SecurityConfig(AuthSuccessHandler authSuccessHandler) {
//        this.authSuccessHandler = authSuccessHandler;
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable) //token을 쓰는 방식이므로 csrf를 disable
//                .cors(cors -> {}) // ★ 반드시 추가
//                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests //HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
//                        .requestMatchers("/api/user/**").permitAll() // 해당 주소에 대한 요청은 인증 없이 접근 허용.
//                        .anyRequest().authenticated() //나머지 요청들에 대해서는 인증을 받야아 한다.
//                ).formLogin(login -> {
//                    login.loginPage("/api/user/login");
//                    login.usernameParameter("userId");
//                    login.passwordParameter("userPwd");
//                    login.defaultSuccessUrl("http://localhost:5173/",true);
//                });
//        return http.build();
//    }

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    // 암호화 진행  BCrypt
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // JWT
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((auth)->auth.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin((auth)->auth.disable())
                .httpBasic((auth)->auth.disable())
                .authorizeHttpRequests((auth)->auth
                        // 접속 허용 설정
                        .requestMatchers("/api/user/login","/api/user/signup","/").permitAll()
                        .requestMatchers("/api/admin/*").hasRole("ADMIN")
                        .anyRequest().authenticated());

        // 로그인 요청이 filter 지나도록 설정
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 세션 설정 -> JWT에서는 session STATELESS로 유지
        http.sessionManagement((session)->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        // ★ 이거 없으면 Authorization 헤더를 JS가 못 읽음
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}