package com.cooksys.quiz_api.services.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.repositories.AnswerRepository;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuizService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

  private final QuizRepository quizRepository;
  private final QuestionRepository questionRepository;
  private final AnswerRepository answerRepository;
  private final QuizMapper quizMapper;
  private final QuestionMapper questionMapper;

  @Override
  public List<QuizResponseDto> getAllQuizzes() {
    return quizMapper.entitiesToDtos(quizRepository.findAll());
  }

  @Override
  @Transactional // Ensures that all changes are committed together or rolled back on failure.
  public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
      // Convert the parameter to a Quiz entity using the mapper.
      Quiz quiz = quizMapper.requestDtoToEntity(quizRequestDto);
  
      // Save the Quiz entity to the database ** by saving quiz and re-assigning it we get any db generated fields on quiz variable
      quiz = quizRepository.save(quiz);
  
      // Save questions and their answers if they exist.
      if (quiz.getQuestions() != null) {
          for (Question question : quiz.getQuestions()) {
              question.setQuiz(quiz); // Set the relationship with the parent quiz.
              question = questionRepository.save(question); // Save the question to the database.
  
              if (question.getAnswers() != null) {
                  for (Answer answer : question.getAnswers()) {
                      answer.setQuestion(question); // Set the relationship with the parent question.
                      answerRepository.save(answer); // Save the answer to the database.
                  }
              }
          }
      }  
      // Convert the saved Quiz entity to a response DTO and return it.
      return quizMapper.entityToDto(quiz);
  }

  @Override
  @Transactional
  public QuizResponseDto deleteQuiz(Long id) {
      // Fetch the quiz from the database or throw an exception if not found
      Quiz quiz = quizRepository.findById(id)
              .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));
  
      // Delete related questions and their answers before deleting the quiz
      List<Question> questions = quiz.getQuestions();
      if (questions != null && !questions.isEmpty()) {
          for (Question question : questions) {
              // Delete all answers related to the question
              List<Answer> answers = question.getAnswers();
              if (answers != null && !answers.isEmpty()) {
                  for (Answer answer : answers) {
                      answerRepository.delete(answer);
                  }
              }
              // Delete the question after its answers are deleted
              questionRepository.delete(question);
          }
      }
  
      // Delete the quiz itself after related questions and answers are deleted
      quizRepository.delete(quiz);
  
      // Return the deleted quiz as a DTO
      return quizMapper.entityToDto(quiz);
  }

  @Override
  @Transactional
  public QuizResponseDto renameQuiz(Long id, String newName) {

      // fetch the quiz from the database or throw an exception if not found
      Quiz quiz = quizRepository.findById(id)
              .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));

      // update the name to newName
      quiz.setName(newName);

      // save the patched quiz to the db
      quiz = quizRepository.save(quiz);

      return quizMapper.entityToDto(quiz);
  }

    @Override
    public QuestionResponseDto getRandomQuestion(Long id) {

        // Fetch the quiz from the database or throw an exception if not found
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));

        // Get questions associated with the quiz and check if they are empty
        List<Question> questions = quiz.getQuestions();
        if (questions == null || questions.isEmpty()) {
            throw new IllegalStateException("This quiz has no questions");
        }

        // Instantiate Random and get a random question
        Random random = new Random();
        int randomIndex = random.nextInt(questions.size());
        Question randomQuestion = questions.get(randomIndex);

        // Convert the selected question to a response DTO and return it
        return questionMapper.entityToDto(randomQuestion);
    }

    @Override
    @Transactional
    public QuizResponseDto addQuestionToQuiz(Long id, QuestionRequestDto addThisQuestion) {

        // Fetch the quiz from the database or throw an exception if not found
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));

        // Map the QuestionRequestDto to a Question entity and set its relationship with the quiz
        // This maps the incoming DTO to an entity that can be persisted
        Question question = questionMapper.requestDtoToEntity(addThisQuestion);
        question.setQuiz(quiz); // Establishes the bi-directional relationship between the question and the quiz

        // For each answer - add relationship to the question
        if (question.getAnswers() != null) {
            List<Answer> savedAnswers = new ArrayList<>(); // Store answers after saving
            for (Answer answer : question.getAnswers()) {
                answer.setQuestion(question); // Set the relationship between the answer and the question
                answer = answerRepository.save(answer); // Explicitly save each answer to the database
                savedAnswers.add(answer); // Add the saved answer to the list
            }
            question.setAnswers(savedAnswers); // Update the question with the saved answers
        }

        // Save the question to the database - we save it separately to persist the relationship
        question = questionRepository.save(question); // JPA save to ensure the question is stored

        // Add the question to the quiz's list of questions
        // The getter on the quiz ensures we're working with the current question list
        List<Question> questionList = quiz.getQuestions();
        if (questionList == null) {
            // If the question list is null, throw an exception or initialize it as needed
            throw new IllegalStateException("The quiz's question list is not initialized.");
        }

        // Add the new question to the existing question list
        questionList.add(question);
        quiz.setQuestions(questionList); // Use the setter to update the quiz's list of questions

        // Save the updated quiz to the database with the new question included
        quiz = quizRepository.save(quiz); // This ensures the relationship is reflected in the database

        // Convert the updated quiz entity to a response DTO and return it
        return quizMapper.entityToDto(quiz); // Map the saved quiz entity back to a response DTO for the client
    }

    @Override
    @Transactional
    public QuestionResponseDto deleteQuestionFromQuiz(Long quizId, Long questionId) {

        // Fetch the quiz from the database or throw an exception if not found
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new IllegalArgumentException("Quiz not found with this id" + quizId));

        // Fetch the question from the database or throw an exception if not found
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));
        
        // make sure the quesiton belongs to the quiz
        if (!question.getQuiz().getId().equals(quizId)) {
            throw new IllegalArgumentException("Question with id: " + questionId + " does not belong to quiz with id: " + quizId);
        }

        // Delete all answers related to the question
        if (question.getAnswers() != null && !question.getAnswers().isEmpty()) {
            for (Answer answer : question.getAnswers()) {
                answerRepository.delete(answer);
            }
        }

        // JPA method
        quiz.getQuestions().remove(question);

        // JPA method
        quizRepository.save(quiz);

        // Delete the question itself
        questionRepository.delete(question);

        // Return the deleted question as a response DTO
        return questionMapper.entityToDto(question);

    }

}
