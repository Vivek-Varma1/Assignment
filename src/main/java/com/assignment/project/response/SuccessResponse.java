package com.assignment.project.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse<T> {

    private boolean success;

    private String message;

    private T data;

    private LocalDateTime timestamp;
}