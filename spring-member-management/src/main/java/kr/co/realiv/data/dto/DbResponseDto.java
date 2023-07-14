package kr.co.realiv.data.dto;

import kr.co.realiv.data.UserRoleEnum;
import lombok.Getter;

@Getter
public class DbResponseDto {
    private String userId;
    private String password;
    private String userName;
    private String email;
    private UserRoleEnum role;
}
