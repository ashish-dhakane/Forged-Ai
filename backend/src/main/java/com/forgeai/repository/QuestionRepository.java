package com.forgeai.repository;

import com.forgeai.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for individual assessment questions.
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Retrieves all questions associated with an assessment.
    List<Question> findByAssessmentId(Long assessmentId);
}
