package com.example.ailearningassistant.question;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class QuestionGenerationService {

	private final AiQuestionClient aiQuestionClient;

	public QuestionGenerationService(AiQuestionClient aiQuestionClient) {
		this.aiQuestionClient = aiQuestionClient;
	}

	public QuestionGenerationResult generate(QuestionGenerationRequest request) {
		List<MultipleChoiceQuestion> questions = aiQuestionClient.generateQuestions(request.examScope());

		return new QuestionGenerationResult(request.examScope(), questions);
	}
}
