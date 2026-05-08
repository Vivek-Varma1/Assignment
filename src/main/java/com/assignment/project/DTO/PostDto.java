package com.assignment.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;

    private Long authorId;

    private String content;

    private Integer likes;

    private LocalDateTime createdAt;
}