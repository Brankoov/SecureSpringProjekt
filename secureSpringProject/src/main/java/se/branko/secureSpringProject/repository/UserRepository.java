package se.branko.secureSpringProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.branko.secureSpringProject.entity.AppUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}

