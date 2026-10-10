package com.hospital;

import com.hospital.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testPasswordHashingAndVerification() {
        String plainPassword = "admin123Password#";
        String hash = PasswordUtil.hashPassword(plainPassword);

        assertNotNull(hash, "Password hash should not be null.");
        assertNotEquals(plainPassword, hash, "Hash should not match plain password string.");
        assertTrue(PasswordUtil.checkPassword(plainPassword, hash), "Password verification should succeed for valid password.");
        assertFalse(PasswordUtil.checkPassword("wrongPassword", hash), "Password verification should fail for invalid password.");
    }

    @Test
    public void testEmptyPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(""));
    }
}
