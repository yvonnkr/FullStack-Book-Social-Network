package com.yvolabs.book.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 27/07/2024
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String username);

}
