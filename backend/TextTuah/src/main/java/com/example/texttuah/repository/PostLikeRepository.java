package com.example.texttuah.repository;

import com.example.texttuah.embeddable.PostLikeId;
import com.example.texttuah.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {

}
