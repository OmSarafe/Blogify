package com.blogify.entry.dtos.userDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userPartialRequestDtos {
    @Size(min = 2,max = 15,message = "Username must be Between 2 and 15 characters")
    private String username;

    @Size(min = 6,max = 15,message = "Password must be Between 6 and 15 characters")
    private String password;

    @Email(message = "Email should be Valid")
    private String email;
}
