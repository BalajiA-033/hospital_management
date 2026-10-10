package com.hospital;

import com.hospital.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    @Test
    public void testEmailValidation() {
        assertTrue(ValidationUtil.isValidEmail("john.doe@hospital.org"));
        assertFalse(ValidationUtil.isValidEmail("invalid-email-address"));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    public void testPhoneValidation() {
        assertTrue(ValidationUtil.isValidPhone("9876543210"));
        assertTrue(ValidationUtil.isValidPhone("+19876543210"));
        assertFalse(ValidationUtil.isValidPhone("123"));
        assertFalse(ValidationUtil.isValidPhone("phone123456"));
    }

    @Test
    public void testDateValidation() {
        assertTrue(ValidationUtil.isValidDate("2026-10-09"));
        assertFalse(ValidationUtil.isValidDate("2026/10/09"));
        assertFalse(ValidationUtil.isValidDate("invalid-date"));
    }
}
