package com.cooksys.quiz_api.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QuizResponseDto {

  // for read  operations - pk needed - GET has no header
  private Long id;

  private String name;

  public List<QuestionResponseDto> questions;

}
