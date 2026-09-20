package com.forgeai.repository;

import com.forgeai.entity.Skill;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for user skills and proficiencies.
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    // Fetches all skills tracked for a given user.
    List<Skill> findByUser(User user);

    // Fetches all skills tracked for a given user ID.
    List<Skill> findByUserId(Long userId);

    // Fetches skills filtered by specific engineering category.
    List<Skill> findByUserIdAndCategory(Long userId, String category);
}
