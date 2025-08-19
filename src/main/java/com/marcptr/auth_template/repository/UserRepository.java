package com.marcptr.auth_template.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.marcptr.auth_template.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}