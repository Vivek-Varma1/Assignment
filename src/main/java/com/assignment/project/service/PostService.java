package com.assignment.project.service;

import com.assignment.project.Repository.CommentRepository;
import com.assignment.project.Repository.PostRepository;
import com.assignment.project.customExceptionHandler.ResourceNotFoundException;
import com.assignment.project.model.Comment;
import com.assignment.project.model.InteractionType;
import com.assignment.project.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private    RedisGuardrailService guardrailService;
    @Autowired
    private  ViralityService viralityService;

    public Post createPost(Post post) {

        post.setCreated_at(LocalDateTime.now());

        return postRepository.save(post);
    }

    public Comment addComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Post",
                                "PostId",
                                postId
                        )
                );

        if ("BOT".equals(comment.getAuthorType())) {

            // 1. Horizontal Cap


            guardrailService.checkHorizontalCap(postId);

            // 2. Vertical Cap
            Integer depth =
                    comment.getDepth_level() == null
                            ? 0
                            : comment.getDepth_level();
            guardrailService.checkVerticalCap(depth);

        /*
           Assume:
           comment.authorId = botId
           comment.humanTargetId = humanId
         */

            // 3. Cooldown Cap
            guardrailService.checkCooldownCap(
                    comment.getAuthorId(),
                    comment.getHumanTargetId()
            );

        }

        comment.setPost_id(postId);
        comment.setCreated_at(LocalDateTime.now());
        Comment savedComment=commentRepository.save(comment);
        if("BOT".equals(comment.getAuthorType())){
            viralityService.updateViralityScore(postId,
                    InteractionType.BOT_REPLY);
        }
        else {
            viralityService.updateViralityScore(postId,InteractionType.HUMAN_COMMENT);
        }
        return savedComment;
    }

    public void likePost(Long postId) {
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","PostId",postId));
        post.setLikes(post.getLikes()+1);
        postRepository.save(post);
        viralityService.updateViralityScore(
                postId,
                InteractionType.HUMAN_LIKE
        );
    }
}
