package com.forgeai.repository;

import com.forgeai.entity.InterviewAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Data access layer for answers submitted in an interview.
@Repository
public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {

    // Retrieves the answer for a specific interview question.
    Optional<InterviewAnswer> findByQuestionId(Long questionId);
}
