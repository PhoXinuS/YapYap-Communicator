package com.example.texttuah.controller;

import com.example.texttuah.dto.PostContentDTO;
import com.example.texttuah.dto.PostDTO;
import com.example.texttuah.dto.UserIdentificationDTO;
import com.example.texttuah.entity.Post;
import com.example.texttuah.service.JWTService;
import com.example.texttuah.service.PostService;
import com.example.texttuah.service.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final RequestService requestService;
    private final JWTService jwtService;

    @Autowired
    public PostController(
            PostService postService,
            RequestService requestService,
            JWTService jwtService
    ) {
        this.postService = postService;
        this.requestService = requestService;
        this.jwtService = jwtService;
    }

    @PostMapping("/add")
    public PostDTO addPost(
            HttpServletRequest request,
            @RequestBody PostContentDTO postContent) {
        return postService.addPost(
                postContent,
                jwtService.extractEmail(
                        requestService.getJwtToken(request)
                )
        );
    }

    @DeleteMapping("/remove/{postId}")
    public void deletePost(
            @PathVariable Long postId,
            HttpServletRequest request
    ) {
        postService.deletePost(
                postId,
                jwtService.extractEmail(
                        requestService.getJwtToken(request)
                )
        );
    }

    @PostMapping("")
    public Set<PostDTO> getAllPosts(
            @RequestBody UserIdentificationDTO userIdentificationDTO
    ) {
        return postService.getUserPosts(userIdentificationDTO);
    }

    @PostMapping("/toggle-like/{postId}")
    public boolean toggleLike(
            HttpServletRequest request,
            @PathVariable Long postId
    ) {
        return postService.toggleLike(
                jwtService.extractEmail(
                        requestService.getJwtToken(request)
                ),
                postId
        );
    }

}
