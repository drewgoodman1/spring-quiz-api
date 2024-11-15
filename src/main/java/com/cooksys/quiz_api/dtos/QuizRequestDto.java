package com.cooksys.quiz_api.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QuizRequestDto {

  // here we need basic details for creating or updating a quiz
  // request is for create and update - pk in url

  // name of quiz
  private String name;

  // list of questions included in new / updated quiz
  public List<QuestionResponseDto> questions;

}
