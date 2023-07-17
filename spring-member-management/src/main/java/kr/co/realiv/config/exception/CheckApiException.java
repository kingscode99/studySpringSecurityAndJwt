package kr.co.realiv.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckApiException extends RuntimeException {
    private final ErrorCode errorCode;
}
