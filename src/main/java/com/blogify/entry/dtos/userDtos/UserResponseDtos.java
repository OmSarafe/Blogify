package com.blogify.entry.dtos.userDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDtos {
    private int id;
    private String  username;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
