package com.example.ailearningassistant.question;

import java.util.List;

public record QuestionGenerationResult(
	String examScope,
	List<MultipleChoiceQuestion> questions
) {
}
