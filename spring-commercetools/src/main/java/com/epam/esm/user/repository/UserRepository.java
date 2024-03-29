package com.epam.esm.user.repository;

import com.epam.esm.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String username);
    Optional<User> findByName(String name);
}
