# KCSAT Spring 서버 게이트웨이

## 사용 기술 스택
1. Spring Boot 3.3.4
2. Spring Cloud Gateway MVC
3. Spring Security
4. JWT

## 기술 이용
1. Spring 백엔드 메인 CORS 처리를 이 곳에서 담당.
2. 쿠키에 JWT를 담아 송수신하여 사용자 별로 요청한 AI 문제를 식별하여 가져오도록 한다. 이를 위해 setAllowCredentials(true) 설정으로 쿠키 데이터 송수신 활성화.
3. 로그인 구현을 위해 Spring Security를 사용하였고, 세션 대신 JWT 토큰만을 사용하여 사용자 정보를 저장하여 서버 무상태성을 유지하고, 프론트엔드에서도 쉽게 사용자 정보 출력 로직을 구현하도록 함.
4. Spring Security를 사용하여 Priority를 Admin, User 계층으로 나누었음.
    - 향후 VIP 계층을 추가하여 유료 회원의 경우, Kafka AI 서버에서 생성 우선순위를 부여하는 방식도 생각 중.

## 기술 도입에 대한 생각
1. 결국 JWT도 보안을 위해서는 Redis로 블랙리스트 처리를 해야하는 한계로, Redis에 저장하는 세션과 큰 차이가 없어 보이는 느낌이 든다.
2. Gateway를 통해 인증 로직을 분리시킴으로서, 뒤의 MSA는 JWT 파싱 정도만 수행하고 각자 역할에 집중하는 효과를 가져온다.
3. 현재는 Spring MVC 모델이지만, 추후 Webflux를 사용한 비동기, 넌블로킹 통신으로 동시성 증가 및 I/O 병목 개선 예정.

## 핵심 구현 기능
1. Spring 커뮤니티 서버, Spring AI 생성 서버를 중계하는 Gateway 역할. 이 곳으로 모든 백엔드 API 요청이 들어오고, api 주소에 따라 목적지 서버를 구별하여 해당 서버 쪽으로 전송됨.
2. Spring MSA와 통신하는 모든 데이터의 관문으로, JWT 발급 및 인증을 이 곳에서 수행함.