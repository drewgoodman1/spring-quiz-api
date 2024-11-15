package com.cooksys.quiz_api.mappers;

import java.util.List;
import java.util.ArrayList;

import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Quiz;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface QuizMapper {

  // ** NEW ** converts a quizRequestDTO (POST || PATCH) to a Quiz entity for saving to the database
  // this comes from a client request - dto is given please add to db
  Quiz requestDtoToEntity(QuizRequestDto dto);
  
  // converts a Quiz entity to a quizResponseDTO (GET || DELETE) for returning data to the client
  // this is given to a client - convert what we got from db to dto
  QuizResponseDto entityToDto(Quiz entity);

  // converts a list of Quiz entities to a list of quizResponseDTO's
  // used in service methods that need to return a list of quizResposeDTO's
  List<QuizResponseDto> entitiesToDtos(List<Quiz> entities);

}