package kr.co.realiv.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_VALIDATE_TOKEN("NOT_VALIDATE_TOKEN, 토큰이 유효하지 않습니다."),
    NOT_EXISTS_USER("등록된 사용자가 없습니다."),
    EXISTS_USER("중복된 사용자가 존재합니다."),
    NOT_EQUALS_PASSWORD("비밀번호가 일치하지 않습니다."),
    NOT_EQUALS_ADMIN_KEY("AdminKey가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.errorMessage = errorMessage;
    }
}
