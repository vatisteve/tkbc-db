package io.github.vatisteve.tkbc.db.service.sp;

import io.github.vatisteve.tkbc.db.model.StatisticParameter;
import junit.framework.TestCase;

/**
 * Tests for StoredProcedureParameterIn
 */
public class StoredProcedureParameterInTest extends TestCase {

    public void testNullNameThrowsException() {
        try {
            new StoredProcedureParameterIn<>(null, 1);
            fail("Expected exception for null name");
        } catch (RuntimeException expected) {
            // Apache Validate may throw NPE for null argument, accept any runtime exception
        }
    }

    public void testBlankNameThrowsIllegalArgumentException() {
        try {
            new StoredProcedureParameterIn<>("  ", 1);
            fail("Expected IllegalArgumentException for blank name");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    public void testNullValueThrowsException() {
        try {
            new StoredProcedureParameterIn<Object>("p", null);
            fail("Expected exception for null value");
        } catch (RuntimeException expected) {
            // Apache Validate may throw NPE for null argument, accept any runtime exception
        }
    }

    public void testGettersAndTypeReturnCorrectClass() {
        StoredProcedureParameterIn<Integer> p = new StoredProcedureParameterIn<>("age", 25);
        assertEquals("age", p.getName());
        assertEquals(Integer.valueOf(25), p.getValue());
        assertEquals(Integer.class, p.getType());
    }
}
