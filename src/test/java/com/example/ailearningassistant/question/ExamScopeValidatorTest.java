package com.example.ailearningassistant.question;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExamScopeValidatorTest {

	private final ExamScopeValidator validator = new ExamScopeValidator();

	@Test
	void validateReturnsErrorWhenExamScopeIsBlank() {
		assertThat(validator.validate(" "))
			.contains("시험 범위를 입력해주세요.");
	}

	@Test
	void validateReturnsErrorWhenExamScopeIsTooShort() {
		assertThat(validator.validate("자바"))
			.contains("시험 범위는 최소 5자 이상 입력해주세요.");
	}

	@Test
	void validateReturnsErrorWhenExamScopeIsTooLong() {
		assertThat(validator.validate("자".repeat(1001)))
			.contains("시험 범위는 최대 1000자까지 입력할 수 있습니다.");
	}

	@Test
	void validateReturnsEmptyWhenExamScopeIsValid() {
		assertThat(validator.validate("자바 기본 문법"))
			.isEmpty();
	}

	@Test
	void normalizeTrimsExamScope() {
		assertThat(validator.normalize("  자바 기본 문법  "))
			.isEqualTo("자바 기본 문법");
	}
}
