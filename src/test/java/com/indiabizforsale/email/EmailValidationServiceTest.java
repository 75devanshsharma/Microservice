package com.indiabizforsale.email;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class EmailValidationServiceTest {

    @Test
    public void emailTestMethod() {
        EmailValidationService emailValidationService = new EmailValidationService();
        assertTrue(emailValidationService.isValid("hello@domain.com"));
        assertFalse(emailValidationService.isValid("hello@domain"));
    }

}
