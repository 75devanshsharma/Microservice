package com.indiabizforsale.email;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class EmailValidationServiceTest {

    @Test
    public void emailTestMethod() {
        EmailValidationService ev = new EmailValidationService();
        assertTrue(ev.emailValidate("hi@gmail.com"));
    }

}
