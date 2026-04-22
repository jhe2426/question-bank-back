# ADsP QuestionBank Project <br/>
ADsP 자격증 대비를 위한 문제 풀이 및 학습 관리 웹 서비스로, 기출문제, 모의고사, 오답노트를 통해 사용자 맞춤 학습을 지원하는 시스템입니다.<br/>

## 개발 기간
* 2025.12.01 ~ 2026.04.12(약 5개월) <br/>

## 시연 영상
[시연 영상 링크](https://youtu.be/G0EVMpZNwKE) <br/>

## 주요 기능
#### 인증 / 로그인 페이지
* JWT 기반 로그인 기능 구현 (Access Token / Refresh Token 구조)
* Refresh Token을 Redis에 저장하여 인증 상태 관리
* Spring Security를 활용한 인가 처리 및 예외 응답 구성
  
#### 회원가입 페이지
* Redis를 활용한 인증번호 저장 및 만료 처리
* Solapi SMS API를 이용한 전화번호 인증 기능 구현
* 아이디 중복 확인 및 승인번호 검증 로직 구현
  
#### 메인 페이지
* 단원, 챕터, 난이도 기반 문제 필터링 기능 구현
  
#### 시험(문제 풀이) 페이지
* 문제 조회 및 사용자 답안 선택 기능 구현
* 제한 시간 기반 자동 제출 기능 구현
  
#### 채점 결과 페이지
* groupId 기반 채점 결과 조회 API 구현
  
#### 마이페이지
* 사용자 시험 이력 조회 기능 구현
* 오답 문제 필터링 및 전체 문제 조회 기능 구현
* 기출문제 단원별 점수 및 합격 여부 계산 로직 구현
* 비밀번호 변경 기능 구현

#### 구현 특징 및 문제 해결
* 로그인 후 뒤로가기 상태에서 재로그인 실패 시 발생한 Axios Interceptor 기반 토큰 재발급 무한 요청 문제 해결
* HTTPS 환경에서 쿠키 기반 인증 구조를 적용하고 HttpOnly, Secure 설정을 통해 보안 강화
* 쿠키 기반 인증 구조에서 CSRF 대응을 위한 검증 로직 적용 <br/>


## 기술 스택
- Spring boot 4.0.1
- Java 21
- Lombok
- Spring web
- Spring validation
- Spring security
- Spring data JPA
- Spring data Redis
- JJWT 0.11.5
- MySQL Connector
- Solapi SDK 1.0.3

## 개발 문서
 [개발 문서 링크](https://www.notion.so/28bb8b4ed32a80209a21fc9dc2ea27ae?source=copy_link) <br/>
