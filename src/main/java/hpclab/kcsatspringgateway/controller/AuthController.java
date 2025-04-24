package hpclab.kcsatspringgateway.controller;

import hpclab.kcsatspringgateway.dto.MemberSignInForm;
import hpclab.kcsatspringgateway.exception.ApiResponse;
import hpclab.kcsatspringgateway.exception.SuccessCode;
import hpclab.kcsatspringgateway.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


/**
 * 로그인, 로그아웃을 담당하는 API 클래스입니다.
 * 로그인을 하는 경우, 내부 Community MSA와 상호작용하여 DB와 데이터를 검증합니다.
 * 로그아웃을 하는 경우, 이 곳에서 Redis와 상호작용하여 블랙리스트 처리를 합니다.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인하는 메서드입니다.
     *
     * @param form 로그인을 위한 email 아이디, 비밀번호 양식입니다.
     * @return 로그인에 성공하면 JWT 토큰을 반환하고, 실패하면 UNAUTHORIZED를 반환합니다.
     */
    @PostMapping("/api/signIn")
    public ResponseEntity<ApiResponse<String>> signIn(@RequestBody @Valid MemberSignInForm form) {
        // 로그인 시도 및 토큰 발급
        String token = authService.signIn(form);

        // JWT 토큰을 응답으로 반환
        return ResponseEntity.ok(new ApiResponse<>(true, token, SuccessCode.SIGN_IN_SUCCESS.getCode(), SuccessCode.SIGN_IN_SUCCESS.getMessage()));
    }


    /**
     * 로그아웃하는 메서드입니다.
     *
     * @param token 블랙리스트를 등록하기 위한 JWT 토큰입니다.
     * @return 로그아웃에 성공하면 ok를 반환합니다.
     */
    @PostMapping("/api/signOut")
    public ResponseEntity<ApiResponse<Void>> signOut(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        authService.signOut(token);

        // JWT 토큰을 응답으로 반환
        return ResponseEntity.ok(new ApiResponse<>(true, null, SuccessCode.SIGN_OUT_SUCCESS.getCode(), SuccessCode.SIGN_OUT_SUCCESS.getMessage()));
    }
}
