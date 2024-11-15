package com.cooksys.quiz_api.services;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;

public interface QuizService {
  // for our GET /quiz
  List<QuizResponseDto> getAllQuizzes();
  // for our CREATE /quiz
  QuizResponseDto createQuiz(QuizRequestDto quizRequestDto);
  // for our DELETE /quiz/id
  QuizResponseDto deleteQuiz(Long id);
  // rename PATCH /quiz/id/rename/newName
  QuizResponseDto renameQuiz(Long id, String newName);
  // get random question GET/id/random
  QuestionResponseDto getRandomQuestion(Long id);
  // add question to quiz PATCH /id/add
  QuizResponseDto addQuestionToQuiz(Long id, QuestionRequestDto addThisQuestion);
  // delete question from quiz DELETE/quizId/questionId
  QuestionResponseDto deleteQuestionFromQuiz(Long quizId, Long questionId);  

}
