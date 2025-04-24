package hpclab.kcsatspringgateway.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 모든 에러사항에 대해 중앙 관리하는 열거형 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E001", "입력값이 유효하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E002", "사용자를 찾을 수 없습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "E003", "아이디 또는 비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "E004", "이미 존재하는 이메일입니다."),
    USER_VERIFICATION_FAILED(HttpStatus.FORBIDDEN, "E005", "사용자 인증에 실패하였습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}