package com.example.ailearningassistant.openai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

class OpenAiQuestionResponseParserTest {

	private final OpenAiQuestionResponseParser parser = new OpenAiQuestionResponseParser(new ObjectMapper());

	@Test
	void parseReturnsTenQuestionsFromOutputText() {
		String responseBody = """
			{
			  "output_text": "%s"
			}
			""".formatted(escapedQuestionsJson(10));

		var questions = parser.parse(responseBody);

		assertThat(questions).hasSize(10);
		assertThat(questions.getFirst().number()).isEqualTo(1);
		assertThat(questions.getFirst().choices()).hasSize(4);
		assertThat(questions.getFirst().answerNumber()).isEqualTo(1);
	}

	@Test
	void parseThrowsExceptionWhenQuestionCountIsNotTen() {
		String responseBody = """
			{
			  "output_text": "%s"
			}
			""".formatted(escapedQuestionsJson(1));

		assertThatThrownBy(() -> parser.parse(responseBody))
			.isInstanceOf(OpenAiApiException.class)
			.hasMessageContaining("객관식 문제 10개");
	}

	private String escapedQuestionsJson(int count) {
		StringBuilder builder = new StringBuilder();
		builder.append("{\\\"questions\\\":[");
		for (int index = 1; index <= count; index++) {
			if (index > 1) {
				builder.append(",");
			}
			builder.append("""
				{\\\"content\\\":\\\"문제 %d\\\",\\\"choices\\\":[\\\"보기 1\\\",\\\"보기 2\\\",\\\"보기 3\\\",\\\"보기 4\\\"],\\\"answerNumber\\\":1,\\\"explanation\\\":\\\"해설\\\"}
				""".formatted(index).trim());
		}
		builder.append("]}");
		return builder.toString();
	}
}
