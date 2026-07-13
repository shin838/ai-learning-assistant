package com.example.ailearningassistant.question;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class ExamScopeValidator {

	private static final int MIN_LENGTH = 5;
	private static final int MAX_LENGTH = 1000;

	public Optional<String> validate(String examScope) {
		if (examScope == null || examScope.isBlank()) {
			return Optional.of("시험 범위를 입력해주세요.");
		}

		String trimmedExamScope = examScope.trim();
		if (trimmedExamScope.length() < MIN_LENGTH) {
			return Optional.of("시험 범위는 최소 " + MIN_LENGTH + "자 이상 입력해주세요.");
		}

		if (trimmedExamScope.length() > MAX_LENGTH) {
			return Optional.of("시험 범위는 최대 " + MAX_LENGTH + "자까지 입력할 수 있습니다.");
		}

		return Optional.empty();
	}

	public String normalize(String examScope) {
		if (examScope == null) {
			return "";
		}
		return examScope.trim();
	}
}
