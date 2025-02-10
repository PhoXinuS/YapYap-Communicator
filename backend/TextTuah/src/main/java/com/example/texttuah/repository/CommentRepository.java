package com.example.texttuah.repository;

import com.example.texttuah.dto.CommentDTO;
import com.example.texttuah.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> getCommentsByCommentedPost_Id(Long commentedPostId);
}
