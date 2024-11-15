package com.cooksys.quiz_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AnswerRequestDto {

    // The text of the answer to be created or updated
    private String text;

    // Indicates if the answer is correct or not
    private boolean correct;
}
