package com.blogify.entry.dtos.postDtos;

import com.blogify.entry.dtos.userDtos.UserAuthorResponseDtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private int id;
    private String title;
    private String description;
    private Set<String> tags;
    private LocalDateTime createddate;
    private LocalDateTime lastmodifieddate;
    private UserAuthorResponseDtos author;
    private String imageUrl;
}
