package com.blogify.entry.controllers;

import com.blogify.entry.enteties.Post;
import com.blogify.entry.enteties.Tag;
import com.blogify.entry.services.PostService;
import com.blogify.entry.dtos.postDtos.PostRequestDtos;
import com.blogify.entry.dtos.postDtos.PostResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostControllers {

    @Autowired
    PostService postServices;
//
//    final private PostService postServices;

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
            ){
       Page <PostResponseDto> posts= postServices.getAll(page, size, sortdirection, sortby).map(post->postServices.convertToPostResponseDtos(post));
       return  ResponseEntity.ok(posts);
    }

    @PostMapping("")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDtos postRequestDtos){
        Post postResponse= postServices.create(postServices.covertToPost(postRequestDtos));
        return new ResponseEntity<PostResponseDto>(postServices.convertToPostResponseDtos(
                postResponse), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public  ResponseEntity<List<PostResponseDto>> searchPosts(@RequestParam String valve){
        List<Post> searchResults=postServices.search(valve);
        List<PostResponseDto> Posts = searchResults.stream().map(postServices::convertToPostResponseDtos).toList();
        return ResponseEntity.ok().body(Posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable int id){
        return  new ResponseEntity<PostResponseDto>(postServices.convertToPostResponseDtos(
                postServices.getPost(id)),
                HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<List<PostResponseDto>>getPostByTitle(@RequestParam String value){
        return  new ResponseEntity<List<PostResponseDto>>(postServices.getByTitle(value).stream().map
                (post -> postServices.convertToPostResponseDtos(post)).collect(Collectors.toList()),HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity<List<PostResponseDto>>getPostByTagName(@RequestParam String value){
        return  new ResponseEntity<List<PostResponseDto>>(postServices.getByTagName(value).stream().map
                (post -> postServices.convertToPostResponseDtos(post)).collect(Collectors.toList()),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePostById(@PathVariable int id, @RequestBody PostRequestDtos postRequestDtos){
        Post postResponse = postServices.update(id, postServices.covertToPost(postRequestDtos));
        return new ResponseEntity<PostResponseDto> (postServices.convertToPostResponseDtos(postResponse), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<PostResponseDto>  updatePartialPostById(@PathVariable int id, @RequestBody PostRequestDtos postRequestDtos){
        Post oldPost = postServices.getPost(id);
        oldPost.setTitle(postRequestDtos.getTitle() != null ? postRequestDtos.getTitle() : oldPost.getTitle());
        oldPost.setDescription(postRequestDtos.getDescription() != null ? postRequestDtos.getDescription() : oldPost.getDescription());
        oldPost.setTags(!postRequestDtos.getTags().isEmpty()?
                postRequestDtos.getTags().stream().map(name -> new Tag(name)).collect(Collectors.toSet()) : oldPost.getTags() );
        Post postResponse = postServices.update(id, oldPost);
        return new ResponseEntity<PostResponseDto>(postServices.convertToPostResponseDtos(postResponse) , HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deletePostById(@PathVariable int id){
        postServices.delete(id);
        return new ResponseEntity<>("Post Deleted Successfully", HttpStatus.NO_CONTENT);
    }


}
