package com.example.ailearningassistant.openai;

import java.util.List;
import java.util.Map;

import com.example.ailearningassistant.question.AiQuestionClient;
import com.example.ailearningassistant.question.MultipleChoiceQuestion;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "openai")
public class OpenAiQuestionClient implements AiQuestionClient {

	private static final String RESPONSES_PATH = "/v1/responses";
	private static final String DEFAULT_MODEL = "gpt-5-mini";

	private final OpenAiProperties openAiProperties;
	private final OpenAiQuestionResponseParser responseParser;
	private final RestClient restClient;

	public OpenAiQuestionClient(OpenAiProperties openAiProperties, OpenAiQuestionResponseParser responseParser) {
		this.openAiProperties = openAiProperties;
		this.responseParser = responseParser;
		this.restClient = RestClient.builder()
			.baseUrl("https://api.openai.com")
			.build();
	}

	@Override
	public List<MultipleChoiceQuestion> generateQuestions(String examScope) {
		if (!openAiProperties.hasApiKey()) {
			throw new OpenAiApiException("OpenAI API 키가 설정되지 않았습니다.");
		}

		try {
			String responseBody = restClient.post()
				.uri(RESPONSES_PATH)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiProperties.apiKey())
				.contentType(MediaType.APPLICATION_JSON)
				.body(requestBody(examScope))
				.retrieve()
				.body(String.class);

			if (responseBody == null || responseBody.isBlank()) {
				throw new OpenAiApiException("OpenAI 응답이 비어 있습니다.");
			}

			return responseParser.parse(responseBody);
		}
		catch (OpenAiApiException exception) {
			throw exception;
		}
		catch (RestClientResponseException exception) {
			throw new OpenAiApiException("OpenAI API 호출에 실패했습니다. "
				+ "상태 코드: " + exception.getStatusCode()
				+ ", 응답: " + shorten(exception.getResponseBodyAsString()), exception);
		}
		catch (RestClientException exception) {
			throw new OpenAiApiException("OpenAI API 호출에 실패했습니다.", exception);
		}
	}

	private String shorten(String responseBody) {
		if (responseBody == null || responseBody.isBlank()) {
			return "응답 본문 없음";
		}

		String compactResponseBody = responseBody.replaceAll("\\s+", " ").trim();
		if (compactResponseBody.length() <= 500) {
			return compactResponseBody;
		}
		return compactResponseBody.substring(0, 500) + "...";
	}

	private Map<String, Object> requestBody(String examScope) {
		return Map.of(
			"model", model(),
			"input", prompt(examScope),
			"text", Map.of(
				"format", Map.of(
					"type", "json_schema",
					"name", "multiple_choice_questions",
					"strict", true,
					"schema", responseSchema()
				)
			)
		);
	}

	private String model() {
		if (openAiProperties.model() == null || openAiProperties.model().isBlank()) {
			return DEFAULT_MODEL;
		}
		return openAiProperties.model();
	}

	private String prompt(String examScope) {
		return """
			너는 학습 평가용 객관식 문제를 만드는 교육 전문가다.

			아래 시험 범위를 바탕으로 객관식 문제 10개를 한국어로 생성해라.

			요구사항:
			- 각 문제는 보기 4개를 가진다.
			- 정답 번호는 1부터 4 사이의 숫자다.
			- 해설은 왜 정답인지 짧고 명확하게 설명한다.
			- 너무 쉬운 암기형보다 개념 이해를 확인하는 문제를 우선한다.
			- 반드시 지정된 JSON Schema에 맞춰 응답한다.

			시험 범위:
			%s
			""".formatted(examScope);
	}

	private Map<String, Object> responseSchema() {
		return Map.of(
			"type", "object",
			"additionalProperties", false,
			"required", List.of("questions"),
			"properties", Map.of(
				"questions", Map.of(
					"type", "array",
					"minItems", 10,
					"maxItems", 10,
					"items", Map.of(
						"type", "object",
						"additionalProperties", false,
						"required", List.of("content", "choices", "answerNumber", "explanation"),
						"properties", Map.of(
							"content", Map.of("type", "string"),
							"choices", Map.of(
								"type", "array",
								"minItems", 4,
								"maxItems", 4,
								"items", Map.of("type", "string")
							),
							"answerNumber", Map.of(
								"type", "integer",
								"minimum", 1,
								"maximum", 4
							),
							"explanation", Map.of("type", "string")
						)
					)
				)
			)
		);
	}
}
