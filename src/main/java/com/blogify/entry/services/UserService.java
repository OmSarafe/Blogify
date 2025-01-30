package com.blogify.entry.services;

import com.blogify.entry.dtos.userDtos.UserRequestDtos;
import com.blogify.entry.dtos.userDtos.UserResponseDtos;
import com.blogify.entry.enteties.User;
import com.blogify.entry.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    UserRepositories userRepositories;

    public User create(User user){
        if (userRepositories.findByUsername(user.getUsername()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"user with username "+user.getUsername()+" is already existing");
        }
        return userRepositories.save(user);
    }

    public Page<User> getAll(int page, int size, String sortDirection, String sortBy){
        Pageable pageable= PageRequest.of(page,size,Sort.by(Sort.Direction.fromString(sortDirection),sortBy));
        return userRepositories.findAll(pageable);
    }

    public User getById(int id){
        return userRepositories.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"user with id: "+id+" not found"));
    }

    public User update(int id,User user){
        User existingUser=getById(id);
        if(user.getUsername() !=null){
            existingUser.setUsername(user.getUsername());
        }
        if(user.getPassword() !=null){
            existingUser.setPassword(user.getPassword());
        }
        if(user.getEmail() !=null){
            existingUser.setEmail(user.getEmail());
        }
        return userRepositories.save(existingUser);
    }

    public void delete(int id){
        getById(id);
        userRepositories.deleteById(id);
    }

    public  User covertToUser(UserRequestDtos userRequestDtos){
        User user=new User();
        user.setUsername(userRequestDtos.getUsername());
        user.setPassword(userRequestDtos.getPassword());
        user.setEmail(userRequestDtos.getEmail());
        return  user;
    }

    public UserResponseDtos covertToUserResponseDto(User user){
        UserResponseDtos userResponseDtos=new UserResponseDtos();
        userResponseDtos.setId(user.getId());
        userResponseDtos.setUsername(user.getUsername());
        userResponseDtos.setPassword(user.getPassword());
        userResponseDtos.setEmail(user.getEmail());
        userResponseDtos.setCreatedDate(user.getCreateDate());
        userResponseDtos.setLastModifiedDate(user.getLastmodifieddate());
        return userResponseDtos;
    }
}
