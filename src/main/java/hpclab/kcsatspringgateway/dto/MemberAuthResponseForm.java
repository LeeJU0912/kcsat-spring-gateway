package hpclab.kcsatspringgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 내부 Community MSA에 로그인 요청을 하여 반환되는 DB 데이터 DTO 클래스입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthResponseForm {
    private String email;
    private String username;
    private String role;
}