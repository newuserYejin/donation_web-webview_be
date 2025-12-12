package com.myproject.mybackend.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    private Key key;

    // @Value 로 application.yml에 지정된 변수값 가져오기
    // JWTUtil이 호출될 때 생성자로 secretKey 생성
    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

//        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 정보 인증
    public String getUsername(String token) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    // 토큰 생성
    public String createJwt(String username, String role, Long expiredMs) {

        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)      // 담아둘 정보
                .setIssuedAt(new Date(System.currentTimeMillis()))      // 발행시간
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))        // 토큰 유효 시간
                .signWith(key, SignatureAlgorithm.HS256)        // 암호화
                .compact();
    }
}
