package com.csc309.arspace;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SignUpUnitTest {
    @Test
    public void testConfirmPassword() {
        SignUp signUp = new SignUp();
        String pw = "password";
        String confirm = "password";
        assertTrue(signUp.checkPassword(pw, confirm));
    }

    @Test
    public void testValidateEntries() {
        SignUp signUp = new SignUp();
        String email = "email";
        String password = "password";
        String confirmPassword = "password";
        assertTrue(signUp.validateEntries(email, password, confirmPassword));
    }
}
