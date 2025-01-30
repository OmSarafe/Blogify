package com.blogify.entry.dtos.postDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDtos {
    private String title;
    private String description;
    private Set<String> tags= new HashSet<>();

}
