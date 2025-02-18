package com.blogify.entry.services;

import com.blogify.entry.dtos.userDtos.UserAuthorResponseDtos;
import com.blogify.entry.enteties.Post;
import com.blogify.entry.enteties.Tag;
import com.blogify.entry.enteties.User;
import com.blogify.entry.repositories.PostRepositories;
import com.blogify.entry.dtos.postDtos.PostRequestDtos;
import com.blogify.entry.dtos.postDtos.PostResponseDto;
import com.blogify.entry.repositories.TagsRepositories;
import com.blogify.entry.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class PostService {

    @Autowired
    PostRepositories postRepositories;

    @Autowired
    TagsRepositories tagsRepositories;

    @Autowired
            @Lazy
    UserService userService;

    @Autowired
    private UserRepositories userRepositories;

    @Autowired
    ImageService imageService;

    private Set<Tag>persistTags(Set<Tag>tags){
        return tags.stream().map(tag->tagsRepositories.findByName(tag.getName()).orElseGet(()->tagsRepositories.save(tag))).collect(Collectors.toSet());
    }

    public Post create(Post post, MultipartFile image, int userId){
        if (image == null || image.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No image provided");
        }
        User user = userService.getById(userId);
        post.setTags(persistTags(post.getTags()));
        post.setUser(user);

        try {
            post.setImageUrl(imageService.saveImage(image));
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.printf("e"+e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to upload image");
        }
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
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + id+ " not found"));
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
    public Post update(int id, Post post,int userId){
        User user = userService.getById(userId);
        Post existingpost = user.getPosts().stream().filter(dbUser -> dbUser.getId() == id).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id: " + id + " not Found"));
        if (post.getTitle()!=null){
            existingpost.setTitle(post.getTitle());
        }
        if (post.getDescription()!=null){
            existingpost.setDescription(post.getDescription());
        }
        if (post.getTags().isEmpty()){
            existingpost.setTags(persistTags(post.getTags()));
        }
        return postRepositories.save(existingpost);
    }

    public void delete(int id,int userId){
        User user = userService.getById(userId);
        Post existingPost=user.getPosts().stream().filter(post -> post.getId()== id).
                findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Post With id :"+id+" not found"));
        user.getPosts().removeIf(post -> post.getId() ==id);
        existingPost.setTags(new HashSet<>());
        postRepositories.delete(existingPost);
        userRepositories.save(user);
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
        PostResponseDto postResponseDto=new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setDescription(post.getDescription());
        postResponseDto.setTags(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        postResponseDto.setAuthor(covertToUserAuthorResponseDto(post.getUser()));
        postResponseDto.setCreateddate(post.getCreateddate());
        postResponseDto.setLastmodifieddate(post.getLastmodifieddate());
        postResponseDto.setImageUrl(post.getImageUrl());
        return postResponseDto;
    }

    public UserAuthorResponseDtos covertToUserAuthorResponseDto(User user){
        UserAuthorResponseDtos userAuthorResponseDtos=new UserAuthorResponseDtos();
        userAuthorResponseDtos.setId(user.getId());
        userAuthorResponseDtos.setUsername(user.getUsername());
        return userAuthorResponseDtos;
    }
}








