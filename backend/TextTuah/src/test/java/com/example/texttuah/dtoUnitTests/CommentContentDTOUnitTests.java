package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.dto.CommentContentDTO;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class CommentContentDTOUnitTests {

    @Test
    void testSettersAndGetters() {
        CommentContentDTO dto = new CommentContentDTO("Test comment content");

        assertEquals("Test comment content", dto.getContent());

        dto.setContent("New comment content");

        assertEquals("New comment content", dto.getContent());
    }
}