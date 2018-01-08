package com.indiabizforsale.email;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;


public class EmailValidationServiceTest {

    @Test
    public void emailTestMethod() {
        EmailValidationService ev = new EmailValidationService();
        PayLoad payLoad = new PayLoad();
        ArrayList<Recipient> to = new ArrayList<>();
        Recipient recipient = new Recipient();
        recipient.setEmail("hello@domain.com");
        to.add(recipient);
        payLoad.setTo(to);
        assertTrue(ev.emailValidate(payLoad.getTo().get(0).getRawEmail()));
    }

}
