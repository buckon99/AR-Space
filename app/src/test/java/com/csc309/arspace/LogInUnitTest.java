package com.csc309.arspace;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LogInUnitTest {
    @Test
    public void testCheckForPassword() {
        Login login = new Login();
        String password = "password";
        assertTrue(login.checkForPassword(password));
    }

    @Test
    public void testCheckPassword() {
        Login login = new Login();
        String email = "email";
        String password = "password";
        assertTrue(login.checkAllFields(email, password));
    }
}
