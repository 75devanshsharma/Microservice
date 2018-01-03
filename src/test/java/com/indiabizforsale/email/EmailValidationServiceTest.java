package com.indiabizforsale.email;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class EmailValidationServiceTest {

    @Test
    public void emailTestMethod() {
        EmailValidationService ev = new EmailValidationService();
        assertTrue(ev.emailValidate("hi@gmail.com"));
    }

}
