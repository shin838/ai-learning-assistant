package com.example.ailearningassistant.question;

import java.util.List;

public record MultipleChoiceQuestion(
	int number,
	String content,
	List<String> choices,
	int answerNumber,
	String explanation
) {
}
