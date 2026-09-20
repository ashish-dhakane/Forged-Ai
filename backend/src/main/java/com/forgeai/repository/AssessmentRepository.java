package com.forgeai.repository;

import com.forgeai.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for technical assessments.
@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    // Retrieves assessments belonging to a specific engineering category.
    List<Assessment> findByCategory(String category);

    // Retrieves assessments matching a difficulty level.
    List<Assessment> findByDifficulty(String difficulty);
}
