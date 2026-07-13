package com.example.ailearningassistant.openai;

public class OpenAiApiException extends RuntimeException {

	public OpenAiApiException(String message) {
		super(message);
	}

	public OpenAiApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
