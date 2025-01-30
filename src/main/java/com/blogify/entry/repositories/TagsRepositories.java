package com.blogify.entry.repositories;

import com.blogify.entry.enteties.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagsRepositories extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByName(String name);
    Optional<Tag> findById(int id);
}
