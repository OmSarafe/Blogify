package com.blogify.entry.services;

import com.blogify.entry.enteties.Post;
import com.blogify.entry.enteties.Tag;
import com.blogify.entry.repositories.PostRepositories;
import com.blogify.entry.dtos.postDtos.PostRequestDtos;
import com.blogify.entry.dtos.postDtos.PostResponseDto;
import com.blogify.entry.repositories.TagsRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    PostRepositories postRepositories;

    @Autowired
    TagsRepositories tagsRepositories;

    public Post create(Post post){
        Set<Tag> persistedTags = post.getTags().stream().
                map(tag -> tagsRepositories.findByName(tag.getName()).orElseGet(()
                        ->tagsRepositories.save(tag))).collect(Collectors.toSet());

        post.setTags(persistedTags);
        return postRepositories.save(post);
    }

//    public List<Post> getAll(){
//        return postRepositories.findAll();
//    }

    public Page<Post> getAll(int page, int size, String sortdirection, String sortby){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortdirection), sortby));
        return  postRepositories.findAll(pageable);
    }

    public Post getPost(int id) {
        return postRepositories.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id+ " not found"));
    }

    public List<Post>search(String value){
        return postRepositories.searchPosts(value);
    }
    public  List<Post> getByTitle(String title){
//        return  postRepositories.findByTitle(title);
//       return  postRepositories.findbyTitleContaining(title);
        return  postRepositories.findbyTitlenative(title);
    }


    public  List<Post> getByTagName(String tagName){
        return  postRepositories.findByTagsName(tagName);
    }
    public Post update(int id, Post post){
        getPost(id);
        post.setId(id);
        Set<Tag> persistedTags = post.getTags().stream().map(
                tag -> tagsRepositories.findByName(tag.getName()).orElseGet(()->tagsRepositories.save(tag))).collect(Collectors.toSet());
        post.setTags(persistedTags);
        return postRepositories.save(post);
    }

    public void delete(int id){
        Post post = postRepositories.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + id+ " not found "));
                post.setTags(new HashSet<>());
                postRepositories.save(post);
        postRepositories.deleteById(id);
    }

    public Post covertToPost(PostRequestDtos postRequestDtos) {
        Post post=new Post();
        post.setTitle(postRequestDtos.getTitle());
        post.setDescription(postRequestDtos.getDescription());
        post.setTags(postRequestDtos.getTags().stream().map(tag ->
                new Tag(tag.toLowerCase())).collect(Collectors.toSet()));
        return post;
    }
    public PostResponseDto convertToPostResponseDtos(Post post) {
        return  new PostResponseDto(post.getId(), post.getTitle(), post.getDescription(),
                post.getTags().stream().map(tag -> tag.getName()).collect(Collectors
                        .toSet()),post.getCreateddate(),post.getLastmodifieddate());
    }
}
