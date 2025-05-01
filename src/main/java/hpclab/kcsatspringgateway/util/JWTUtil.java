package hpclab.kcsatspringgateway.util;

import hpclab.kcsatspringgateway.dto.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

/**
 * JWT 토큰 설정 관련 클래스입니다.
 */
@Component
public class JWTUtil {

    // AccessToken 15분 제한
    private static final long accessTokenValidity = 1000 * 60 * 15;
    // RefreshToken 1일 제한
    private static final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 1;

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey key;

    @PostConstruct
    private void init() {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("JWT secret must not be null or blank");
        }
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public static final String USER_EMAIL = "userEmail";
    public static final String USER_NAME = "userName";
    public static final String ROLE = "role";

    // GUEST Access 토큰 생성 메서드
    public String generateGuestAccessToken() {
        return Jwts.builder()
                .claim(USER_EMAIL, "guest@csatmaker.site")
                .claim(USER_NAME, "GUEST")
                .claim(ROLE, Role.ROLE_GUEST.getValue())
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간
                .signWith(key)
                .compact();
    }

    // Access 토큰 생성 메서드
    public String generateAccessToken(String userEmail, String userName, String role) {
        return Jwts.builder()
                .claim(USER_EMAIL, userEmail)
                .claim(USER_NAME, userName)
                .claim(ROLE, role)
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    // Refresh 토큰 생성 메서드
    public String generateRefreshToken(String userEmail, String userName, String role) {
        return Jwts.builder()
                .claim(USER_EMAIL, userEmail)
                .claim(USER_NAME, userName)
                .claim(ROLE, role)
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }

    // claim 반환 메서드
    public Claims getClaims(String token) {
        String tokenWithoutHeader = token.replace("Bearer ", "");
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(tokenWithoutHeader)
                .getPayload();
    }

    // 토큰 유효기간 반환 메서드
    public long getExpiration(String token) {
        String tokenWithoutHeader = token.replace("Bearer ", "");
        Claims claims = getClaims(tokenWithoutHeader);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    // 토큰 유효기간 확인 메서드
    public boolean isTokenExpired(String tokenWithHeader) {
        try {
            String token = tokenWithHeader.replace("Bearer ", "");
            Claims claims = getClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
