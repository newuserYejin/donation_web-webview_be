package com.myproject.mybackend.user.controller;

import com.myproject.mybackend.common.ResponseMessage;
import com.myproject.mybackend.jwt.AuthDetails;
import com.myproject.mybackend.jwt.util.JWTUtil;
import com.myproject.mybackend.user.model.dto.UserDTO;
import com.myproject.mybackend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.Charset;
import java.util.*;

@Controller
@RequestMapping(value = "/api/user")
@Tag(name = "User API", description = "사용자 관련 API")
public class userController {

    @Autowired
    UserService userService;
    @Autowired
    private JWTUtil jWTUtil;

    @RequestMapping(value = "/signup", produces="application/json;charset=UTF-8")
    @ResponseBody
    public void signup(HttpServletRequest request, HttpServletResponse response, Locale locale, Model model,
                       @RequestBody Map<String, Object> params){

        System.out.println("params = " + params);

        userService.signUpUser(params);
    }

    @RequestMapping(value = "/userInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseMessage> userInfo(HttpServletRequest request, HttpServletResponse response, Locale locale, Model model){
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("토큰 없음");
        }

        String token = authHeader.substring(7);

        String userId = jWTUtil.getUsername(token);

        UserDTO user = userService.findUserById(userId);

        Map<String,Object> result = new HashMap<>();
        result.put("user",user);

        return ResponseEntity.ok()
                .body(new ResponseMessage(200,"회원 정보 조회 성공", result));
    }

}
