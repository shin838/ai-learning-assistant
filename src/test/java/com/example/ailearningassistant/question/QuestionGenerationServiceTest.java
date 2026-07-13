package com.example.ailearningassistant.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class QuestionGenerationServiceTest {

	private final AiQuestionClient aiQuestionClient = Mockito.mock(AiQuestionClient.class);
	private final QuestionGenerationService questionGenerationService = new QuestionGenerationService(aiQuestionClient);

	@Test
	void generateReturnsTenMultipleChoiceQuestions() {
		given(aiQuestionClient.generateQuestions("Java 21 기본 문법"))
			.willReturn(List.of(
				new MultipleChoiceQuestion(
					1,
					"Java 문제입니다.",
					List.of("보기 1", "보기 2", "보기 3", "보기 4"),
					1,
					"해설입니다."
				)
			));

		QuestionGenerationResult result = questionGenerationService.generate(
			new QuestionGenerationRequest("Java 21 기본 문법")
		);

		assertThat(result.examScope()).isEqualTo("Java 21 기본 문법");
		assertThat(result.questions()).hasSize(1);
		assertThat(result.questions().getFirst().choices()).hasSize(4);
		assertThat(result.questions().getFirst().answerNumber()).isEqualTo(1);
	}
}
