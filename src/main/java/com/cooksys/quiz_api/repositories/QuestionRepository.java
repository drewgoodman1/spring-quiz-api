package com.cooksys.quiz_api.repositories;

import com.cooksys.quiz_api.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Add custom query methods if needed
}

