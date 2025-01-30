package com.blogify.entry.repositories;

import com.blogify.entry.enteties.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositories extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
}
