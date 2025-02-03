package com.blogify.entry.dtos.exceptionDtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private  String field;
    private String errorMessage;
}
