package com.assignment.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long post_id;

    private Long authorId;

    private String authorType;

    @Lob
    private String content;

    private Long parentCommentId;

    private Integer depth_level;

    private LocalDateTime created_at;

    private Long humanTargetId;

    @PrePersist
    public void prePersist() {
        created_at = LocalDateTime.now();
    }
}
