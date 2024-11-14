# KCSAT Spring 서버 게이트웨이

## 사용 기술 스택
1. Spring Boot 3.3.4
2. Spring Cloud Gateway

## 기술 이용
1. 백엔드 메인 CORS 처리를 이 곳에서 담당.
2. 쿠키에 SessionID를 담아 송수신 하여 사용자 별로 요청한 AI 문제를 식별하여 가져오도록 한다.
3. 이를 위해 setAllowCredentials(true) 설정으로 쿠키 데이터 송수신 활성화.

## 핵심 구현 기능
1. Spring 커뮤니티 서버, Spring AI 생성 서버를 중계하는 Gateway 역할. 이 곳으로 모든 백엔드 API 요청이 들어오고, api 주소에 따라 목적지 서버를 구별하여 해당 서버 쪽으로 전송된다.