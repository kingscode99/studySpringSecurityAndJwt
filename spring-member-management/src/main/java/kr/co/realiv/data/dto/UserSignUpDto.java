package kr.co.realiv.data.dto;

import lombok.Getter;

@Getter
public class UserSignUpDto {
    private String userId;
    private String password;
    private String userName;
    private String email;
    private String role;
}
