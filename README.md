# DebugMate MVP

주니어 개발자가 오류 코드와 로그를 기록하고 해결 상태를 관리하는 Spring Boot 기반 MVP입니다. AI 연동은 아직 포함하지 않았습니다.

## 기술 구성

- Java 17
- Spring Boot 3.3
- Thymeleaf
- Spring Data JPA
- MySQL 8

## 실행 준비

1. MySQL Workbench 또는 MySQL 명령창에서 `database/setup.sql`을 실행합니다.
2. IntelliJ에서 `pom.xml`을 프로젝트로 엽니다.
3. MySQL의 root 비밀번호가 있다면 실행 환경변수 `DB_PASSWORD`에 설정합니다.
4. `DebugMateApplication`을 실행하고 `http://localhost:8080`에 접속합니다.

환경변수로 연결 정보를 바꿀 수 있습니다.

```text
DB_URL=jdbc:mysql://localhost:3306/debugmate?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=root
DB_PASSWORD=본인의 MySQL 비밀번호
```

## 현재 기능

- 오류 정보 등록
- MySQL 저장
- 오류 기록 조회 및 검색
- 해결 상태 변경
- 반응형 공통 UI

AI 분석 버튼은 의도적으로 비활성 상태이며 이후 OpenAI API 연동 지점으로 사용합니다.

## 로그인과 역할

- JWT는 JavaScript에서 읽을 수 없는 HttpOnly 쿠키로 전달됩니다.
- 주니어 계정은 가입 즉시 로그인할 수 있습니다.
- 시니어 계정은 가입 후 승인 대기 상태가 되며 관리자가 승인해야 로그인할 수 있습니다.
- 관리자 계정은 `ADMIN_EMAIL`, `ADMIN_PASSWORD` 환경변수로 최초 실행 시 생성됩니다.
- 시니어 지원자는 화면에 표시된 `hanbyeol5002@gmail.com`으로 경력과 포트폴리오를 별도 제출합니다.

화면 확인용 `preview` 프로필에서만 관리자 초기 비밀번호는 `admin1234!`입니다. 일반 실행에서는 반드시 환경변수로 관리자 비밀번호와 JWT 비밀키를 설정하세요.

## 전역 프론트엔드 규칙

자세한 규칙은 `docs/FRONTEND_RULES.md`를 참고하세요.
