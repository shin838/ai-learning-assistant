# AI Learning Assistant

시험 범위를 입력하면 객관식 문제 10개를 생성하는 Spring Boot + Thymeleaf 웹 애플리케이션입니다.

## 기술 스택

- Java 21
- Spring Boot 4.1.0
- Gradle
- Thymeleaf
- Lombok

DB, JPA, Security는 현재 사용하지 않습니다.

## 실행 모드

기본 실행 모드는 `dummy`입니다.

```properties
ai.provider=${AI_PROVIDER:dummy}
```

### dummy 모드

OpenAI API 키 없이 개발할 수 있는 모드입니다.

```bash
./gradlew bootRun
```

브라우저에서 접속합니다.

```text
http://localhost:8080
```

시험 범위를 입력하고 `문제 생성` 버튼을 누르면 예시 객관식 문제 10개가 표시됩니다.

### openai 모드

OpenAI API를 실제로 호출하는 모드입니다.

```bash
AI_PROVIDER=openai OPENAI_API_KEY=본인_API_키 OPENAI_MODEL=gpt-5-mini ./gradlew bootRun
```

주의사항:

- API 키는 `sk-...` 또는 `sk-proj-...` 형태입니다.
- API 키를 채팅, 코드, Git 커밋에 남기지 마세요.
- ChatGPT 구독과 OpenAI API 결제/쿼터는 별도입니다.
- `insufficient_quota` 오류가 나오면 OpenAI Platform의 Billing과 Usage Limits를 확인해야 합니다.

## STS에서 실행하기

dummy 모드는 별도 환경변수 없이 실행하면 됩니다.

openai 모드를 사용하려면 STS Run Configuration의 `Environment` 탭에 아래 값을 추가합니다.

```text
AI_PROVIDER=openai
OPENAI_API_KEY=본인_API_키
OPENAI_MODEL=gpt-5-mini
```

그 다음 `AiLearningAssistantApplication`을 실행합니다.

## 테스트

```bash
./gradlew test
```

성공 기준:

```text
BUILD SUCCESSFUL
```

## 주요 URL

```text
GET  /
POST /questions/generate
GET  /api/health
```

## 현재 동작 흐름

1. 사용자가 `/` 화면에서 시험 범위를 입력합니다.
2. `POST /questions/generate` 요청이 서버로 전달됩니다.
3. `QuestionGenerationService`가 설정된 AI 클라이언트를 호출합니다.
4. `dummy` 모드에서는 예시 문제 10개를 생성합니다.
5. `openai` 모드에서는 OpenAI Responses API를 호출합니다.
6. 결과 화면에서 문제, 보기, 정답, 해설을 표시합니다.
