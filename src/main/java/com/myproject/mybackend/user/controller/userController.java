package com.myproject.mybackend.user.controller;

import com.myproject.mybackend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/user")
@Tag(name = "User API", description = "사용자 관련 API")
public class userController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/signup", produces="application/json;charset=UTF-8")
    @ResponseBody
    public void signup(HttpServletRequest request, HttpServletResponse response, Locale locale, Model model,
                       @RequestBody Map<String, Object> params){

        System.out.println("params = " + params);

        userService.signUpUser(params);
    }

    @RequestMapping(value = "/login", produces="application/json;charset=UTF-8")
    @ResponseBody
    public void login(HttpServletRequest request, HttpServletResponse response, Locale locale, Model model,
                       @RequestBody Map<String, Object> params){

        System.out.println("params = " + params);

        userService.loginUser(params);
    }

}
