package com.backend.legisloop.entities;

import com.backend.legisloop.model.Legislation;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LegislationEntityTest {

    @Test
    void testToModel_withNullSessionId_shouldNotThrow() {
        // Arrange: Create an instance of LegislationEntity.
        LegislationEntity legislationEntity = new LegislationEntity();
        
        // Simulate a null value in the database for session_id using Reflection.
        // This bypasses normal type checking and mimics a DB null for a primitive.
        ReflectionTestUtils.setField(legislationEntity, "session_id", null);
        
        // Act & Assert: Converting the entity to a model should NOT throw an exception.
        // Currently, because session_id is a primitive, this conversion fails,
        // and the test will fail. Once fixed (e.g., by using Integer instead of int),
        // the test should pass.
        assertDoesNotThrow(() -> {
            Legislation model = legislationEntity.toModel();
        });
    }
}
