package com.baraNajjar.codeEditor.repositories;

import com.baraNajjar.codeEditor.entites.User;
import com.baraNajjar.codeEditor.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String username);

    List<User> findAllByRole(Role role);
}
