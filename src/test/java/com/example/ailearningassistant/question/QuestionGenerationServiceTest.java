package com.example.ailearningassistant.question;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class QuestionGenerationServiceTest {

	private final QuestionGenerationService questionGenerationService = new QuestionGenerationService();

	@Test
	void generateReturnsTenMultipleChoiceQuestions() {
		QuestionGenerationResult result = questionGenerationService.generate(
			new QuestionGenerationRequest("Java 21 기본 문법")
		);

		assertThat(result.examScope()).isEqualTo("Java 21 기본 문법");
		assertThat(result.questions()).hasSize(10);
		assertThat(result.questions().getFirst().choices()).hasSize(4);
		assertThat(result.questions().getFirst().answerNumber()).isEqualTo(1);
	}
}
