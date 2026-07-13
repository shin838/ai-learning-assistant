package com.example.ailearningassistant.openai;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class OpenAiPropertiesTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withUserConfiguration(TestConfiguration.class);

	@Test
	void bindsOpenAiProperties() {
		contextRunner
			.withPropertyValues(
				"openai.api-key=test-api-key",
				"openai.model=test-model"
			)
			.run(context -> {
				OpenAiProperties properties = context.getBean(OpenAiProperties.class);

				assertThat(properties.apiKey()).isEqualTo("test-api-key");
				assertThat(properties.model()).isEqualTo("test-model");
				assertThat(properties.hasApiKey()).isTrue();
			});
	}

	@Test
	void hasApiKeyReturnsFalseWhenApiKeyIsBlank() {
		contextRunner
			.withPropertyValues(
				"openai.api-key=",
				"openai.model=test-model"
			)
			.run(context -> {
				OpenAiProperties properties = context.getBean(OpenAiProperties.class);

				assertThat(properties.hasApiKey()).isFalse();
			});
	}

	@EnableConfigurationProperties(OpenAiProperties.class)
	static class TestConfiguration {
	}
}
