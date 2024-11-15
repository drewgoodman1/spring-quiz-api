package com.cooksys.quiz_api.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QuestionRequestDto {

    // The text of the question to be created or updated
    private String text;

    // Optional list of answers to be included with the question
    private List<AnswerRequestDto> answers;
}
