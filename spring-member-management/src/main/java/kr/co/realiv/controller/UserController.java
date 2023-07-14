package kr.co.realiv.controller;

import kr.co.realiv.config.security.UserDetailsImpl;
import kr.co.realiv.data.dto.*;
import kr.co.realiv.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto> signUp(@RequestBody UserSignUpDto userSignupDto) {
        return ResponseEntity.ok(new ResponseDto(userService.signUp(userSignupDto)));
    }

    @PostMapping("/logIn")
    public ResponseEntity<ResponseDto> logIn(@RequestBody UserLogInDto userLogInDto, HttpServletResponse response) {
        return ResponseEntity.ok(new ResponseDto(userService.logIn(userLogInDto, response)));
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDto> update(@RequestBody UserUpdateAndDeleteDto userUpdateAndDeleteDto,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails,
                                    HttpServletResponse response) {
        return ResponseEntity.ok(new ResponseDto(userService.update(userUpdateAndDeleteDto, userDetails, response)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> delete(@RequestBody UserUpdateAndDeleteDto userUpdateAndDeleteDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ResponseDto(userService.delete(userUpdateAndDeleteDto, userDetails)));
    }
}
