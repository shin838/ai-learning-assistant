package com.example.ailearningassistant.question;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class QuestionGenerationExceptionHandler {

	@ExceptionHandler(AiQuestionClientException.class)
	public String handleAiQuestionClientException(
		AiQuestionClientException exception,
		HttpServletRequest request,
		Model model
	) {
		model.addAttribute("errorMessage", exception.getMessage());
		model.addAttribute("examScope", normalize(request.getParameter("examScope")));
		return "index";
	}

	private String normalize(String examScope) {
		if (examScope == null) {
			return "";
		}
		return examScope.trim();
	}
}
