package com.example.ailearningassistant.question;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

@Service
public class QuestionGenerationService {

	public QuestionGenerationResult generate(QuestionGenerationRequest request) {
		List<MultipleChoiceQuestion> questions = IntStream.rangeClosed(1, 10)
			.mapToObj(number -> createQuestion(number, request.examScope()))
			.toList();

		return new QuestionGenerationResult(request.examScope(), questions);
	}

	private MultipleChoiceQuestion createQuestion(int number, String examScope) {
		return new MultipleChoiceQuestion(
			number,
			"[" + examScope + "] 범위에서 확인할 핵심 개념 문제 " + number + "번입니다.",
			List.of(
				"보기 1",
				"보기 2",
				"보기 3",
				"보기 4"
			),
			1,
			"현재는 OpenAI 연동 전이므로 더미 해설을 표시합니다."
		);
	}
}
