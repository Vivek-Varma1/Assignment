package com.assignment.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {

    private Long postId;

    private Long authorId;

    private String authorType;

    private String content;

    private Long parentCommentId;

    private Integer depthLevel;
}