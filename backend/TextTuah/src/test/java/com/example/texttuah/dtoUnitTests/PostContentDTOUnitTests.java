package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.dto.PostContentDTO;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class PostContentDTOUnitTests {


    @Test
    void testSettersAndGetters() {
        PostContentDTO dto = new PostContentDTO("Test post content");

        assertEquals("Test post content", dto.getContent());

        dto.setContent("New post content");

        assertEquals("New post content", dto.getContent());
    }
}