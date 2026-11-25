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
public class UserRequestDtos {
    @NotBlank(message = "Username cant be blank")
    @Size(min = 2,max = 15,message = "Username must be Between 2 and 15 characters")
    private String username;
    @NotBlank(message = "Password cant be blank")
    @Size(min = 6,max = 15,message = "Password must be Between 6 and 15 characters")
    private String password;
    @Email(message = "Email should be Valid")
    @NotEmpty(message = "Email cannot be Empty")
    private String email;

}
