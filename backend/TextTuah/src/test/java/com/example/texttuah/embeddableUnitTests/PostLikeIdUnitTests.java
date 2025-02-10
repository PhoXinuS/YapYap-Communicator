package com.example.texttuah.embeddableUnitTests;

import com.example.texttuah.embeddable.PostLikeId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostLikeIdUnitTests {

    @Test
    void testPostLikeIdCreation() {
        PostLikeId postLikeId = new PostLikeId(1L, 2L);

        assertNotNull(postLikeId);
        assertEquals(1L, postLikeId.getPostId());
        assertEquals(2L, postLikeId.getLikerId());
    }

    @Test
    void testEqualsAndHashCode() {
        PostLikeId postLikeId1 = new PostLikeId(1L, 2L);
        PostLikeId postLikeId2 = new PostLikeId(1L, 2L);
        PostLikeId postLikeId3 = new PostLikeId(2L, 3L);

        assertEquals(postLikeId1, postLikeId2);
        assertNotEquals(postLikeId1, postLikeId3);
        assertEquals(postLikeId1.hashCode(), postLikeId2.hashCode());
        assertNotEquals(postLikeId1.hashCode(), postLikeId3.hashCode());
    }

    @Test
    void testSettersAndGetters() {
        PostLikeId postLikeId = new PostLikeId();
        postLikeId.setPostId(1L);
        postLikeId.setLikerId(2L);

        assertEquals(1L, postLikeId.getPostId());
        assertEquals(2L, postLikeId.getLikerId());
    }
}