package kr.co.realiv.service;

import kr.co.realiv.config.exception.CheckApiException;
import kr.co.realiv.config.exception.ErrorCode;
import kr.co.realiv.config.jwt.JwtUtil;
import kr.co.realiv.config.security.UserDetailsImpl;
import kr.co.realiv.data.User;
import kr.co.realiv.data.UserRoleEnum;
import kr.co.realiv.data.dto.DbResponseDto;
import kr.co.realiv.data.dto.UserLogInDto;
import kr.co.realiv.data.dto.UserSignUpDto;
import kr.co.realiv.data.dto.UserUpdateAndDeleteDto;
import kr.co.realiv.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    @Value("${admin.secret.key}")
    private String adminKey;

    public String signUp(UserSignUpDto userSignupDto) {
        String encodedPassword = passwordEncoder.encode(userSignupDto.getPassword());
        UserRoleEnum roleEnum = isAdmin(userSignupDto.getRole());
        User user = new User(userSignupDto, roleEnum, encodedPassword);
        if (findUser(user.getUserId()) != null) {
            throw new CheckApiException(ErrorCode.EXISTS_USER);
        }
        mapper.signUp(user);
        return "회원가입 완료";
    }

    @Transactional(readOnly = true)
    public String logIn(UserLogInDto userLogInDto, HttpServletResponse response) {
        User user = new User(userLogInDto);
        DbResponseDto foundUser = findUser(user.getUserId());
        if (foundUser == null) {
            throw new CheckApiException(ErrorCode.NOT_EXISTS_USER);
        }
        if (!passwordEncoder.matches(userLogInDto.getPassword(), foundUser.getPassword())) {
            throw new CheckApiException(ErrorCode.NOT_EQUALS_PASSWORD);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUserId(), foundUser.getRole()));
        return "로그인 완료";
    }

    @Transactional
    public String update(UserUpdateAndDeleteDto userUpdateAndDeleteDto,
                         UserDetailsImpl userDetails,
                         HttpServletResponse response) {
        checkDuplicationUserId(userUpdateAndDeleteDto);
        if (!userDetails.isAdmin()) {
            checkPassword(userUpdateAndDeleteDto, userDetails);
        }
        updateUserTable(userUpdateAndDeleteDto, userDetails);
        if (userDetails.isAdmin()) {
            if (userUpdateAndDeleteDto.isSameUser(userDetails.getUser())) {
                response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(userUpdateAndDeleteDto.getUpdateUserId(), UserRoleEnum.ADMIN));
                return "관리자 권한으로 수정 하였습니다.";
            }
            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(userDetails.getUsername(), UserRoleEnum.ADMIN));
            return "관리자 권한으로 수정 하였습니다.";
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(userUpdateAndDeleteDto.getUpdateUserId(), userDetails.getUser().getRole()));
        return "수정 완료";
    }

    @Transactional
    public String delete(UserUpdateAndDeleteDto userUpdateAndDeleteDto, UserDetailsImpl userDetails) {
        if (!userDetails.isAdmin()) {
            checkPassword(userUpdateAndDeleteDto, userDetails);
        }
        mapper.deleteUser(userUpdateAndDeleteDto);
        if (userDetails.isAdmin()) {
            return "관리자 권한으로 삭제 하였습니다.";
        }
        return "삭제 완료";
    }

    private void checkPassword(UserUpdateAndDeleteDto userUpdateAndDeleteDto, UserDetailsImpl userDetails) {
        if (!passwordEncoder.matches(userUpdateAndDeleteDto.getCheckPassword(), userDetails.getPassword())) {
            throw new CheckApiException(ErrorCode.NOT_EQUALS_PASSWORD);
        }
    }

    private void updateUserTable(UserUpdateAndDeleteDto userUpdateAndDeleteDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        userUpdateAndDeleteDto.isEmpty(user, passwordEncoder);
        mapper.updateUserId(userUpdateAndDeleteDto);
    }

    private void checkDuplicationUserId(UserUpdateAndDeleteDto userUpdateAndDeleteDto) {
        if (findUser(userUpdateAndDeleteDto.getUpdateUserId()) != null) {
            throw new CheckApiException(ErrorCode.EXISTS_USER);
        }
    }

    private DbResponseDto findUser(String userId) {
        return mapper.findByUserId(userId);
    }

    private UserRoleEnum isAdmin(String inputAdminKey) {
        UserRoleEnum userRoleEnum = UserRoleEnum.USER;
        if (inputAdminKey.equals(adminKey)) {
            userRoleEnum = UserRoleEnum.ADMIN;
        }
        if (!inputAdminKey.equals("") && !inputAdminKey.equals(adminKey)) {
            throw new CheckApiException(ErrorCode.NOT_EQUALS_ADMIN_KEY);
        }
        return userRoleEnum;
    }
}
