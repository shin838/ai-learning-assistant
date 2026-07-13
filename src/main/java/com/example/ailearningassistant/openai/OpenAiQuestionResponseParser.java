package com.example.ailearningassistant.openai;

import java.util.ArrayList;
import java.util.List;

import com.example.ailearningassistant.question.MultipleChoiceQuestion;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class OpenAiQuestionResponseParser {

	private final ObjectMapper objectMapper;

	public OpenAiQuestionResponseParser(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public List<MultipleChoiceQuestion> parse(String responseBody) {
		try {
			JsonNode root = objectMapper.readTree(responseBody);
			String outputText = extractOutputText(root);
			JsonNode questionsNode = objectMapper.readTree(outputText).path("questions");

			if (!questionsNode.isArray() || questionsNode.size() != 10) {
				throw new OpenAiApiException("OpenAI 응답에서 객관식 문제 10개를 찾지 못했습니다.");
			}

			List<MultipleChoiceQuestion> questions = new ArrayList<>();
			for (int index = 0; index < questionsNode.size(); index++) {
				questions.add(toQuestion(index + 1, questionsNode.get(index)));
			}
			return questions;
		}
		catch (OpenAiApiException exception) {
			throw exception;
		}
		catch (Exception exception) {
			throw new OpenAiApiException("OpenAI 응답을 문제 형식으로 변환하지 못했습니다.", exception);
		}
	}

	private String extractOutputText(JsonNode root) {
		JsonNode outputTextNode = root.path("output_text");
		if (outputTextNode.isTextual()) {
			return outputTextNode.asText();
		}

		for (JsonNode output : root.path("output")) {
			for (JsonNode content : output.path("content")) {
				if ("output_text".equals(content.path("type").asText()) && content.path("text").isTextual()) {
					return content.path("text").asText();
				}
			}
		}

		throw new OpenAiApiException("OpenAI 응답에서 output_text를 찾지 못했습니다.");
	}

	private MultipleChoiceQuestion toQuestion(int number, JsonNode questionNode) {
		String content = requiredText(questionNode, "content");
		List<String> choices = choices(questionNode.path("choices"));
		int answerNumber = questionNode.path("answerNumber").asInt();
		String explanation = requiredText(questionNode, "explanation");

		if (answerNumber < 1 || answerNumber > 4) {
			throw new OpenAiApiException("정답 번호는 1부터 4 사이여야 합니다.");
		}

		return new MultipleChoiceQuestion(number, content, choices, answerNumber, explanation);
	}

	private String requiredText(JsonNode node, String fieldName) {
		JsonNode field = node.path(fieldName);
		if (!field.isTextual() || field.asText().isBlank()) {
			throw new OpenAiApiException("OpenAI 응답에 필수 항목이 없습니다: " + fieldName);
		}
		return field.asText();
	}

	private List<String> choices(JsonNode choicesNode) {
		if (!choicesNode.isArray() || choicesNode.size() != 4) {
			throw new OpenAiApiException("각 문제는 보기 4개를 가져야 합니다.");
		}

		List<String> choices = new ArrayList<>();
		for (JsonNode choiceNode : choicesNode) {
			if (!choiceNode.isTextual() || choiceNode.asText().isBlank()) {
				throw new OpenAiApiException("보기는 빈 값일 수 없습니다.");
			}
			choices.add(choiceNode.asText());
		}
		return choices;
	}
}
