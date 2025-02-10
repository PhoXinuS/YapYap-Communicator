package com.example.texttuah.service;

import com.example.texttuah.dto.PostContentDTO;
import com.example.texttuah.dto.PostDTO;
import com.example.texttuah.dto.UserIdentificationDTO;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Post;
import com.example.texttuah.entity.PostLike;
import com.example.texttuah.repository.PostLikeRepository;
import com.example.texttuah.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final PostLikeRepository postLikeRepository;

    @Autowired
    public PostService(
            PostRepository postRepository,
            UserService userService,
            PostLikeRepository postLikeRepository
    ) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.postLikeRepository = postLikeRepository;
    }

    private Post createPostByPostContent(
            @NonNull PostContentDTO postContentDTO,
            @NonNull ChatUser poster) {
        Post post = new Post();
        post.setContent(postContentDTO.getContent());
        post.setPoster(poster);
        return post;
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public PostDTO addPost(
            @NonNull PostContentDTO postContent,
            @NonNull String email) {
        if (postContent == null || email == null) {
            return null;
        }
        ChatUser chatUser = userService.findByEmail(email);
        if (chatUser == null) {
            return null;
        }

        Post post = createPostByPostContent(postContent, chatUser);
        postRepository.save(post);
        return PostDTO.convertTo(post, chatUser);
    }

    public void deletePost(Long postId, String email) {
        ChatUser user = userService.findByEmail(email);
        if (user == null) {
            return;
        }
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return;
        }
        if (!post.getPoster().equals(user)) {
            return;
        }
        postRepository.delete(post);
    }

    public Set<PostDTO> getUserPosts(UserIdentificationDTO userIdentificationDTO) {
        ChatUser user = userService.findByUserIdentification(userIdentificationDTO);
        if (user == null) {
            return null;
        }
        return PostDTO.convertTo(user.getPosts(), user);
    }

    public boolean toggleLike(String email, Long postId) {
        ChatUser user = userService.findByEmail(email);
        Post post = postRepository.findById(postId).orElse(null);
        if (user == null || post == null) {
            return false;
        }

        PostLike like = new PostLike(post, user);
        if (user.getPostLikes().contains(like)) {
            postLikeRepository.delete(like);
            return false;
        } else {
            postLikeRepository.save(like);
            return true;
        }
    }
}
