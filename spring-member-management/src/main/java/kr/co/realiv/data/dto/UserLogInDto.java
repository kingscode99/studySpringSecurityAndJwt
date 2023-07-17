package kr.co.realiv.data.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class UserLogInDto {
    private String userId;
    private String password;
}
