package com.myproject.mybackend.user.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

    private String userId;
    private String userPwd;
    private String userName;
    private String userAuth;
    private String userTel;
    private String userEmail;

    public UserDTO(String userId, String userPwd, String userAuth) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.userAuth = userAuth;
    }

}
