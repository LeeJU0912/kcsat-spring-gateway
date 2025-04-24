package hpclab.kcsatspringgateway.service;

import hpclab.kcsatspringgateway.dto.MemberAuthResponseForm;
import hpclab.kcsatspringgateway.dto.MemberSignInForm;
import hpclab.kcsatspringgateway.dto.Role;
import hpclab.kcsatspringgateway.exception.ApiException;
import hpclab.kcsatspringgateway.exception.ErrorCode;
import hpclab.kcsatspringgateway.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;


/**
 * 회원 인증 관련 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JWTUtil jwtUtil;
    private final WebClient webClient;

    private final RedisTemplate<String, String> redisTemplate;

    private static final String BLACKLIST_PREFIX = "blacklist:";

    /**
     * 로그인 관련 로직입니다.
     *
     * @param form 로그인 입력 폼 데이터입니다.
     * @return 로그인에 성공하는 경우, JWT 토큰 데이터를 반환합니다. 로그인에 실패하는 경우, UNAUTHORIZED 에러를 발생합니다.
     */
    public String signIn(MemberSignInForm form) {
        MemberAuthResponseForm member = webClient
                .post()
                .uri("/api/member/internal/signIn")
                .bodyValue(form)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ApiException(ErrorCode.LOGIN_FAILED)))
                .bodyToMono(MemberAuthResponseForm.class)
                .block();

        if (member == null) {
            throw new ApiException(ErrorCode.USER_VERIFICATION_FAILED);
        }

        return jwtUtil.generateToken(member.getEmail(), member.getUsername(), member.getRole());
    }

    /**
     * 게스트 로그인 처리 메서드입니다.
     * @return JWT 토큰 발급
     */
    public String guestLogin() {
        return jwtUtil.generateToken(null, null, Role.ROLE_GUEST.getValue());
    }


    /**
     * 로그아웃 처리 메서드입니다. 남은 토큰 유효기간동안 Redis를 통해 블랙리스트 처리를 합니다.
     *
     * @param token JWT 토큰 데이터
     */
    public void signOut(String token) {
        String uuid = jwtUtil.getClaims(token).getId();

        // 토큰을 블랙리스트에 저장 (만료 시간까지 유지)
        redisTemplate.opsForValue()
                .set(BLACKLIST_PREFIX + uuid, "true", jwtUtil.getExpiration(token), TimeUnit.MILLISECONDS);
    }

    /**
     * 현재 접속하는 JWT 토큰 사용자가 블랙리스트인지 검증하는 메서드입니다.
     *
     * @param token JWT 토큰 데이터
     * @return 블랙리스트라면 true, 그렇지 않다면 false를 반환합니다.
     */
    public boolean isTokenBlacklisted(String token) {
        String uuid = jwtUtil.getClaims(token).getId();
        return redisTemplate.hasKey(BLACKLIST_PREFIX + uuid);
    }
}
