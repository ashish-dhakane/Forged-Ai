package com.forgeai.repository;

import com.forgeai.entity.RepositoryEntity;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// Data access layer for synced GitHub repository entities.
@Repository
public interface RepositoryEntityRepository extends JpaRepository<RepositoryEntity, Long> {

    // Retrieves all repositories synced for a user.
    List<RepositoryEntity> findByUser(User user);

    // Retrieves all repositories synced for a user ID.
    List<RepositoryEntity> findByUserId(Long userId);

    // Finds a repository by user ID and repository name.
    Optional<RepositoryEntity> findByUserIdAndName(Long userId, String name);
}
