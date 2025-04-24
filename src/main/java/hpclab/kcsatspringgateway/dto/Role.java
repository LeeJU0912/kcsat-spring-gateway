package hpclab.kcsatspringgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 권한을 나타내는 열거형 클래스입니다.
 * <p>권한 목록</p>
 * <ul>
 *     <li>ROLE_ADMIN: 관리자</li>
 *     <li>ROLE_MANAGER: 매니저(부관리자)</li>
 *     <li>USER: 일반 회원</li>
 *     <li>GUEST: 비회원</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public enum Role {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_USER("ROLE_USER"),
    ROLE_GUEST("ROLE_GUEST");

    private final String value;
}
