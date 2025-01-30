package com.blogify.entry.repositories;

import com.blogify.entry.enteties.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositories extends JpaRepository<Post,Integer> {
    @EntityGraph(value = "Post.tags")
    List<Post> findByTitle(String title);
    @EntityGraph(value = "Post.tags")
    List<Post> findByTagsName(String tagName);

    @EntityGraph(value = "Post.tags")
    List<Post> findByTagsId(int id);

    @Override
    @EntityGraph(value = "Post.tags")
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(value = "Post.tags")
    @Query("Select p FROM posts p WHERE p.title LIKE %:title%")
    List<Post>findbyTitleContaining(@Param("title")String title);

    @Query(value = "Select * FROM posts WHERE title like %?1%",nativeQuery = true)
    List<Post>findbyTitlenative(String title);

    @EntityGraph(value = "Post.tags")
    @Query("SELECT DISTINCT p FROM posts p LEFT JOIN p.tags t " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Post> searchPosts(@Param("search") String search);

}
