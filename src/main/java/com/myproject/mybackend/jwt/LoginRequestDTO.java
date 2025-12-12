package com.myproject.mybackend.jwt;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequestDTO {

    private String userId;
    private String userPwd;

}
