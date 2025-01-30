package com.blogify.entry.controllers;

import com.blogify.entry.dtos.userDtos.UserRequestDtos;
import com.blogify.entry.dtos.userDtos.UserResponseDtos;
import com.blogify.entry.enteties.User;
import com.blogify.entry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDtos>>getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "ASC") String sortdirection,
            @RequestParam(defaultValue = "id") String sortby
    ){
        Page<UserResponseDtos> allUsers =userService.getAll(page,size,sortdirection,sortby).map(user->userService.covertToUserResponseDto(user));
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    public ResponseEntity<UserResponseDtos> createUser(@RequestBody UserRequestDtos userRequestDtos){
        User user=userService.covertToUser(userRequestDtos);
        User createduser= userService.create(user);
        UserResponseDtos userResponseDtos=userService.covertToUserResponseDto(createduser);
        return ResponseEntity.status(201).body(userResponseDtos);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deletebyid(@PathVariable int id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDtos> updatebyid(@PathVariable int id,@RequestBody UserRequestDtos userRequestDtos){
        User user= userService.covertToUser(userRequestDtos);
        User updateduser=userService.update(id,user);
        UserResponseDtos userResponseDtos=userService.covertToUserResponseDto(updateduser);
        return ResponseEntity.ok(userResponseDtos);
    }
}
