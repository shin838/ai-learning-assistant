package com.example.ailearningassistant.question;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "dummy", matchIfMissing = true)
public class DummyQuestionClient implements AiQuestionClient {

	@Override
	public List<MultipleChoiceQuestion> generateQuestions(String examScope) {
		return IntStream.rangeClosed(1, 10)
			.mapToObj(number -> createQuestion(number, examScope))
			.toList();
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
			"현재는 dummy 모드이므로 예시 해설을 표시합니다."
		);
	}
}
