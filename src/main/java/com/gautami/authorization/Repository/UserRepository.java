package com.gautami.authorization.Repository;

import com.gautami.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    Optional<User> findByUsername(String username);
}
