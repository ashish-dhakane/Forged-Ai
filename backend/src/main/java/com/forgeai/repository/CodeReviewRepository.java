package com.forgeai.repository;

import com.forgeai.entity.CodeReview;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for AI code reviews.
@Repository
public interface CodeReviewRepository extends JpaRepository<CodeReview, Long> {

    // Retrieves all code reviews performed for a user.
    List<CodeReview> findByUser(User user);

    // Retrieves all code reviews for a user ID ordered by newest first.
    List<CodeReview> findByUserIdOrderByCreatedAtDesc(Long userId);
}
