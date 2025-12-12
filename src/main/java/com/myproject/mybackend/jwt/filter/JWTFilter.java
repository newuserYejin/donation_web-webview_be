package com.myproject.mybackend.jwt.filter;

import com.myproject.mybackend.jwt.AuthDetails;
import com.myproject.mybackend.jwt.util.JWTUtil;
import com.myproject.mybackend.user.model.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // token이 있는지 접두사가 Bearer 가 맞는지 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("token null");

            // 다음 필터로 넘기기
            filterChain.doFilter(request,response);

            return;
        }

        String token = authorizationHeader.split(" ")[1];

        // 만료 시간 확인 (소멸되었는지의 여부)
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request,response);
            return;
        }

        String userId = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        // role이 ROLE_ 로 시작하지 않으면 붙이기
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        UserDTO user = new UserDTO(userId,"temporaryPwd",role);

        AuthDetails authDetails = new AuthDetails(user);

       // 인증 token 생성
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(authDetails, null, authDetails.getAuthorities());

        // user 세션 생성
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);
    }
}
