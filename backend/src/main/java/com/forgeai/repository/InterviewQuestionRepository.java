package com.forgeai.repository;

import com.forgeai.entity.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for questions generated during an interview.
@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    // Retrieves all questions asked in an interview session.
    List<InterviewQuestion> findByInterviewId(Long interviewId);
}
