package com.example.ailearningassistant.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openai")
public record OpenAiProperties(
	String apiKey,
	String model
) {

	public boolean hasApiKey() {
		return apiKey != null && !apiKey.isBlank();
	}
}
