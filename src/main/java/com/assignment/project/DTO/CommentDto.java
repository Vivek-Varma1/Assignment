package com.assignment.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private Long postId;

    private Long authorId;

    private String authorType;

    private String content;

    private Long parentCommentId;

    private Integer depthLevel;

    private LocalDateTime createdAt;
}