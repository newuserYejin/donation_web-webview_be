package com.myproject.mybackend.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.mybackend.jwt.AuthDetails;
import com.myproject.mybackend.jwt.LoginRequestDTO;
import com.myproject.mybackend.jwt.util.JWTUtil;
import com.myproject.mybackend.user.model.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;


    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login"); // 여기서 필터가 동작 될 URL 설정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginRequestDTO loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequestDTO.class);

            String userId = loginRequest.getUserId();
            String password = loginRequest.getUserPwd();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password,null);

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AuthDetails userDetails = (AuthDetails) authResult.getPrincipal();

        System.out.println("userDetails = " + userDetails);

        String userId = userDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority grantedAuthority = iterator.next();

        String role = grantedAuthority.getAuthority();

        String token = jwtUtil.createJwt(userId,role,1000L * 60 * 60 * 10);

        response.addHeader("Authorization", "Bearer " + token);

        Map<String, Object> result = new HashMap<>();

        UserDTO user = userDetails.getUser();
        user.setUserPwd("");

        result.put("user",user ); // User 객체를 그대로 JSON으로 반환

        // JSON 응답
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(result));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);

        System.out.println("unsuccessfulAuthentication");
    }
}
