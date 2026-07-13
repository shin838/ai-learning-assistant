package com.example.ailearningassistant.question;

import java.util.List;

public interface AiQuestionClient {

	List<MultipleChoiceQuestion> generateQuestions(String examScope);
}
