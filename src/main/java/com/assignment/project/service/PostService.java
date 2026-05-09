package com.assignment.project.service;

import com.assignment.project.Repository.CommentRepository;
import com.assignment.project.Repository.PostRepository;
import com.assignment.project.customExceptionHandler.ResourceNotFoundException;
import com.assignment.project.model.Comment;
import com.assignment.project.model.InteractionType;
import com.assignment.project.model.Post;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public Post createPost(Post post) {

        post.setCreated_at(LocalDateTime.now());

        return postRepository.save(post);
    }@Transactional
    public Comment addComment(Long postId, Comment comment) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Post",
                                "PostId",
                                postId
                        )
                );

        boolean botInteraction = false;

        try {

            if ("BOT".equals(comment.getAuthorType())) {

                botInteraction = true;

                // 1. Horizontal Cap
                guardrailService.checkHorizontalCap(postId);

                // 2. Vertical Cap
                Integer depth =
                        comment.getDepth_level() == null
                                ? 0
                                : comment.getDepth_level();

                guardrailService.checkVerticalCap(depth);

                // 3. Cooldown Cap
                guardrailService.checkCooldownCap(
                        comment.getAuthorId(),
                        comment.getHumanTargetId()
                );
            }


            comment.setPost_id(postId);

            comment.setCreated_at(LocalDateTime.now());

            Comment savedComment =
                    commentRepository.save(comment);
            if (botInteraction) {

                viralityService.updateViralityScore(
                        postId,
                        InteractionType.BOT_REPLY
                );

                notificationService
                        .handleBotInteractionNotification(
                                post.getAuther_id(),
                                "Bot replied to your post"
                        );

            } else {

                viralityService.updateViralityScore(
                        postId,
                        InteractionType.HUMAN_COMMENT
                );
            }

            return savedComment;

        } catch (Exception e) {

        /*
            Rollback Redis bot counter
         */
            if (botInteraction) {

                guardrailService.rollbackHorizontalCap(
                        postId
                );
            }

            throw e;
        }
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
