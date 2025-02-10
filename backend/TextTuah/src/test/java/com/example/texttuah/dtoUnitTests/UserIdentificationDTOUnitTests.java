package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.dto.UserIdentificationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserIdentificationDTOUnitTests {

    @Mock
    private UserIdentificationDTO userIdentificationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConstructorAndGetters() {
        UserIdentificationDTO dto = new UserIdentificationDTO(1L, "test@example.com");
        assertEquals(1L, dto.getId());
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    void testSetters() {
        UserIdentificationDTO dto = new UserIdentificationDTO();
        dto.setId(1L);
        dto.setEmail("test@example.com");
        assertEquals(1L, dto.getId());
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    void testEquals() {
        UserIdentificationDTO dto1 = new UserIdentificationDTO(1L, "test@example.com");
        UserIdentificationDTO dto2 = new UserIdentificationDTO(1L, "test@example.com");
        UserIdentificationDTO dto3 = new UserIdentificationDTO(2L, "other@example.com");
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        UserIdentificationDTO dto1 = new UserIdentificationDTO(1L, "test@example.com");
        UserIdentificationDTO dto2 = new UserIdentificationDTO(1L, "test@example.com");
        UserIdentificationDTO dto3 = new UserIdentificationDTO(2L, "other@example.com");
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testIsIdentificationByID() {
        UserIdentificationDTO dtoWithId = new UserIdentificationDTO(1L);
        UserIdentificationDTO dtoWithoutId = new UserIdentificationDTO("test@example.com");
        assertTrue(dtoWithId.isIdentificationByID());
        assertFalse(dtoWithoutId.isIdentificationByID());
    }
}