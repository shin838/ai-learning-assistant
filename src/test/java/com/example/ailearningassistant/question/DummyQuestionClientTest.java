package com.example.ailearningassistant.question;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DummyQuestionClientTest {

	private final DummyQuestionClient dummyQuestionClient = new DummyQuestionClient();

	@Test
	void generateQuestionsReturnsTenDummyQuestions() {
		var questions = dummyQuestionClient.generateQuestions("자바 기본 문법");

		assertThat(questions).hasSize(10);
		assertThat(questions.getFirst().content()).contains("자바 기본 문법");
		assertThat(questions.getFirst().choices()).hasSize(4);
		assertThat(questions.getFirst().answerNumber()).isEqualTo(1);
	}
}
