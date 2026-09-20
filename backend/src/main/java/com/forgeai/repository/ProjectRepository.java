package com.forgeai.repository;

import com.forgeai.entity.Project;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for user portfolio projects.
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Retrieves all portfolio projects created by a user.
    List<Project> findByUser(User user);

    // Retrieves all portfolio projects for a given user ID.
    List<Project> findByUserId(Long userId);
}
