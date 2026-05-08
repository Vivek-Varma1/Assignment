package com.assignment.project.controller;

import com.assignment.project.DTO.CreateCommentRequest;
import com.assignment.project.DTO.CreatePostRequest;
import com.assignment.project.model.Comment;
import com.assignment.project.model.Post;
import com.assignment.project.response.ErrorResponse;
import com.assignment.project.response.SuccessResponse;
import com.assignment.project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;


    @PostMapping
    public ResponseEntity<SuccessResponse<Post>> createPost(@RequestBody CreatePostRequest request) {
        Post post=new Post();
        post.setAuther_id(request.getAuthorId());
        post.setContent(request.getContent());
        Post savedPost=postService.createPost(post);
        SuccessResponse<Post> response =
                new SuccessResponse<>(
                        true,
                        "Post created successfully",
                        savedPost,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }
    @PostMapping("/{postId}/comments")
    public ResponseEntity<SuccessResponse<Comment>> addComment(@RequestBody CreateCommentRequest request , @PathVariable Long postId) {
        Comment comment = new Comment();

        comment.setPost_id(postId);
        comment.setAuthorId(request.getAuthorId());
        comment.setAuthorType(request.getAuthorType());
        comment.setContent(request.getContent());
        comment.setParentCommentId(request.getParentCommentId());
        comment.setDepth_level(request.getDepthLevel());

        Comment savedComment =
                postService.addComment(
                        postId,
                        comment
                );
        SuccessResponse<Comment> response =
                new SuccessResponse<>(
                        true,
                        "Comment added successfully",
                        savedComment,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );


    }


    @PostMapping("/{postId}/like")
    public  ResponseEntity<SuccessResponse<String>> likePost(@PathVariable Long postId) {

        postService.likePost(postId);

        SuccessResponse<String> response =
                new SuccessResponse<>(
                        true,
                        "Post liked successfully",
                        null,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

}
