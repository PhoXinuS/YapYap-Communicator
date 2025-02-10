package com.example.texttuah.repository;

import com.example.texttuah.embeddable.CommentLikeId;
import com.example.texttuah.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {
}
