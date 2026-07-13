package com.example.ailearningassistant.openai;

import com.example.ailearningassistant.question.AiQuestionClientException;

public class OpenAiApiException extends AiQuestionClientException {

	public OpenAiApiException(String message) {
		super(message);
	}

	public OpenAiApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
