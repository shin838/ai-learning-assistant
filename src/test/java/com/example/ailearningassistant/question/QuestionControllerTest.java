package com.example.ailearningassistant.question;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import com.example.ailearningassistant.openai.OpenAiApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private QuestionGenerationService questionGenerationService;

	@MockitoBean
	private ExamScopeValidator examScopeValidator;

	@Test
	void generateReturnsResultPage() throws Exception {
		given(examScopeValidator.normalize("  Java 21  "))
			.willReturn("Java 21");
		given(examScopeValidator.validate("Java 21"))
			.willReturn(java.util.Optional.empty());
		given(questionGenerationService.generate(any()))
			.willReturn(new QuestionGenerationResult(
				"Java 21",
				List.of(new MultipleChoiceQuestion(
					1,
					"Java 21 문제입니다.",
					List.of("보기 1", "보기 2", "보기 3", "보기 4"),
					1,
					"해설입니다."
				))
			));

		mockMvc.perform(post("/questions/generate")
				.param("examScope", "  Java 21  "))
			.andExpect(status().isOk())
			.andExpect(view().name("result"))
			.andExpect(content().string(containsString("생성된 객관식 문제")))
			.andExpect(content().string(containsString("Java 21 문제입니다.")));

		verify(questionGenerationService).generate(new QuestionGenerationRequest("Java 21"));
	}

	@Test
	void generateRedirectsToHomeWhenExamScopeIsBlank() throws Exception {
		given(examScopeValidator.normalize(" "))
			.willReturn("");
		given(examScopeValidator.validate(""))
			.willReturn(java.util.Optional.of("시험 범위를 입력해주세요."));

		mockMvc.perform(post("/questions/generate")
				.param("examScope", " "))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(content().string(containsString("시험 범위를 입력해주세요.")));
	}

	@Test
	void generateReturnsInputPageWithErrorMessageWhenOpenAiCallFails() throws Exception {
		given(examScopeValidator.normalize("Java 21"))
			.willReturn("Java 21");
		given(examScopeValidator.validate("Java 21"))
			.willReturn(java.util.Optional.empty());
		given(questionGenerationService.generate(any()))
			.willThrow(new OpenAiApiException("OpenAI API 키가 설정되지 않았습니다."));

		mockMvc.perform(post("/questions/generate")
				.param("examScope", "Java 21"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(content().string(containsString("OpenAI API 키가 설정되지 않았습니다.")));
	}

	@Test
	void generateReturnsInputPageWhenExamScopeIsTooShort() throws Exception {
		given(examScopeValidator.normalize("자바"))
			.willReturn("자바");
		given(examScopeValidator.validate("자바"))
			.willReturn(java.util.Optional.of("시험 범위는 최소 5자 이상 입력해주세요."));

		mockMvc.perform(post("/questions/generate")
				.param("examScope", "자바"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(content().string(containsString("시험 범위는 최소 5자 이상 입력해주세요.")))
			.andExpect(content().string(containsString("자바")));
	}
}
