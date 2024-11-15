package com.cooksys.quiz_api.controllers;

import java.util.List;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.services.QuizService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping
    public List<QuizResponseDto> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @PostMapping
    public QuizResponseDto createQuiz(@RequestBody QuizRequestDto quizRequestDto) {
        // Call the service to create a new quiz
        return quizService.createQuiz(quizRequestDto);
    }

    @DeleteMapping("/{id}")
    public QuizResponseDto deleteQuiz(@PathVariable Long id) {
      return quizService.deleteQuiz(id);
    }

    @PatchMapping("/{id}/rename/{newName}") 
      public QuizResponseDto renameQuiz(@PathVariable Long id, @PathVariable String newName) {
        return quizService.renameQuiz(id, newName);
      }

    @GetMapping("/{id}/random")
      public QuestionResponseDto getRandomQuestion(@PathVariable Long id) {
        return quizService.getRandomQuestion(id);
      }

    @PatchMapping("/{id}/add")
      public QuizResponseDto addQuestionToQuiz(@PathVariable Long id, @RequestBody QuestionRequestDto addThisQuestion) {
        return quizService.addQuestionToQuiz(id, addThisQuestion);
      }
    
    @DeleteMapping("/{id}/delete/{questionId}")
    public QuestionResponseDto deleteQuestionFromQuiz(@PathVariable Long id, @PathVariable Long questionId) {
        return quizService.deleteQuestionFromQuiz(id, questionId);
    }
    
}


  
  // TODO: Implement the remaining 6 endpoints from the documentation.

