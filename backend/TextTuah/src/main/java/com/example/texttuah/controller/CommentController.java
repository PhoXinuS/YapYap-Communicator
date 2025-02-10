package com.example.texttuah.controller;

import com.example.texttuah.dto.CommentContentDTO;
import com.example.texttuah.dto.CommentDTO;
import com.example.texttuah.dto.MessageContentDTO;
import com.example.texttuah.repository.CommentRepository;
import com.example.texttuah.service.CommentService;
import com.example.texttuah.service.JWTService;
import com.example.texttuah.service.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/comments")
public class CommentController {
    private final RequestService requestService;
    private final JWTService jwtService;
    private final CommentService commentService;

    @Autowired
    public CommentController(
            RequestService requestService,
            JWTService jwtService,
            CommentService commentService
    ) {
        this.requestService = requestService;
        this.jwtService = jwtService;
        this.commentService = commentService;
    }

    @GetMapping("/{postId}")
    public Set<CommentDTO> getPostComments(
            @PathVariable Long postId,
            HttpServletRequest request
    ) {
        String email = jwtService.extractEmail(requestService.getJwtToken(request));
        return commentService.getPostComments(postId, email);
    }

    @PostMapping("/send/{postId}")
    public CommentDTO commentPost(
            HttpServletRequest request,
            @PathVariable Long postId,
            @RequestBody CommentContentDTO contentDTO
    ) {
        return commentService.commentPost(
                jwtService.extractEmail(
                        requestService.getJwtToken(
                                request
                        )
                ),
                postId,
                contentDTO
        );
    }

    @DeleteMapping("/remove/{commentId}")
    public void removeComment(
            HttpServletRequest request,
            @PathVariable Long commentId
    ) {
        commentService.removeComment(
            jwtService.extractEmail(
                    requestService.getJwtToken(request)
            ),
            commentId
        );
    }

    @PostMapping("/toggle-like/{commentId}")
    public boolean toggleLike(
            HttpServletRequest request,
            @PathVariable Long commentId
    ) {
        String email = jwtService.extractEmail(requestService.getJwtToken(request));
        return commentService.toggleLike(email, commentId);
    }

}
