package com.myproject.mybackend.user.service;

import com.myproject.mybackend.common.dao.CommonDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String namespace = "user.";

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CommonDAO commonDAO;

    // 회원가입
    public void signUpUser(Map<String, Object> params) {
        System.out.println("service params = " + params);

        if (params.get("userPwd") != null) {
            String enPwd = passwordEncoder.encode(params.get("userPwd").toString());

            params.put("userPwd", enPwd);

            System.out.println("params = " + params);

            int result = commonDAO.insert(namespace.concat("signUp"),params);

            System.out.println("result = " + result);
        } else {
            logger.info("비밀번호 암호화 불가");
        }

    }

    public void loginUser(Map<String, Object> params) {
    }
}
