package kr.co.realiv.data;

import kr.co.realiv.data.dto.DbResponseDto;
import kr.co.realiv.data.dto.UserLogInDto;
import kr.co.realiv.data.dto.UserSignUpDto;
import lombok.Getter;

@Getter
public class User {
    private final String userId;
    private String password;
    private String userName;
    private String email;
    private UserRoleEnum role;

    public User(UserSignUpDto userSignupDto, UserRoleEnum userRoleEnum, String encodedPassword) {
        this.userId = userSignupDto.getUserId();
        this.password = encodedPassword;
        this.userName = userSignupDto.getUserName();
        this.email = userSignupDto.getEmail();
        this.role = userRoleEnum;
    }

    public User(UserLogInDto userLogInDto) {
        this.userId = userLogInDto.getUserId();
    }

    public User(DbResponseDto foundUser) {
        this.userId = foundUser.getUserId();
        this.password = foundUser.getPassword();
        this.userName = foundUser.getUserName();
        this.email = foundUser.getEmail();
        this.role = foundUser.getRole();
    }
}
