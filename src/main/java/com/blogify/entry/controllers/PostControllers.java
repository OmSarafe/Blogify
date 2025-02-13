package com.blogify.entry.controllers;

import com.blogify.entry.enteties.Post;
import com.blogify.entry.enteties.Tag;
import com.blogify.entry.services.PostService;
import com.blogify.entry.dtos.postDtos.PostRequestDtos;
import com.blogify.entry.dtos.postDtos.PostResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostControllers {

    private final PostService postServices;

    @Autowired
    private ObjectMapper objectmapper;


    PostControllers(PostService postService) {
        this.postServices = postService;
    }

//    @GetMapping("")
//    public ResponseEntity<List<PostResponseDto>> getAllPosts(){
//        return new ResponseEntity<List<PostResponseDto>>(postServices.getAll().stream().map
//                (post ->postServices.convertToPostResponseDtos(post)).collect(Collectors.toList()), HttpStatus.OK);
//    }

    @GetMapping("")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "ASC") String sortdirection,
            @RequestParam(defaultValue = "id") String sortby
    ) {
        Page<PostResponseDto> posts = postServices.getAll(page, size, sortdirection, sortby).map(post -> postServices.convertToPostResponseDtos(post));
        return ResponseEntity.ok(posts);
    }

    @PostMapping(value = "/user/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDto> createPost(
            @RequestPart(name = "post", required = true)
            @Valid @NotNull String postRequestDtosString,
            @RequestPart(name = "image") MultipartFile image,
            @PathVariable int userId) {
        PostRequestDtos postRequestDtos = null;
        try {
            postRequestDtos = objectmapper.readValue(postRequestDtosString, PostRequestDtos.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "invalid post data");
        }
        Post createdPost = postServices.create(postServices.covertToPost(postRequestDtos), image, userId);
        return ResponseEntity.status(201).body(postServices.convertToPostResponseDtos(createdPost));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDto>> searchPosts(@RequestParam String valve) {
        List<Post> searchResults = postServices.search(valve);
        List<PostResponseDto> Posts = searchResults.stream().map(postServices::convertToPostResponseDtos).toList();
        return ResponseEntity.ok().body(Posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable int id) {
        return new ResponseEntity<PostResponseDto>(postServices.convertToPostResponseDtos(
                postServices.getPost(id)),
                HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<List<PostResponseDto>> getPostByTitle(@RequestParam String value) {
        return new ResponseEntity<List<PostResponseDto>>(postServices.getByTitle(value).stream().map
                (post -> postServices.convertToPostResponseDtos(post)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity<List<PostResponseDto>> getPostByTagName(@RequestParam String value) {
        return new ResponseEntity<List<PostResponseDto>>(postServices.getByTagName(value).stream().map
                (post -> postServices.convertToPostResponseDtos(post)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/{id}/user/{userId}")
    public ResponseEntity<PostResponseDto> updatePostById(@PathVariable int id, @RequestBody PostRequestDtos postRequestDtos, @PathVariable int userId) {
        Post postResponse = postServices.update(id, postServices.covertToPost(postRequestDtos), userId);
        return new ResponseEntity<PostResponseDto>(postServices.convertToPostResponseDtos(postResponse), HttpStatus.OK);
    }

    @PatchMapping("/{id}/user/{userId}")
    public ResponseEntity<PostResponseDto> updatePartialPostById(@PathVariable int id, @RequestBody PostRequestDtos postRequestDtos, @PathVariable int userId) {
        Post updatedPost = postServices.update(id, postServices.covertToPost(postRequestDtos), userId);
        return ResponseEntity.ok(postServices.convertToPostResponseDtos(updatedPost));
    }

    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<?> deletePostById(@PathVariable int id, @PathVariable int userId) {
        postServices.delete(id, userId);
        return new ResponseEntity<>("Post Deleted Successfully", HttpStatus.NO_CONTENT);
    }


}
