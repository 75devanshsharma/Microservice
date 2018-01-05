package com.indiabizforsale.email;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmailSenderTest {
    @Test
    public void emailTest() throws IOException {
        EmailSender emailSender = new EmailSender();
        PayLoad payLoad = new PayLoad();
        ArrayList<Recipient> to = new ArrayList<>();
        Map<String, String> templateData = new HashMap<>();
        Map<String,String> templateData1 = new HashMap<>();
        templateData.put("name", "Dev");
        templateData1.put("name", "Deva");
        Recipient recipient = new Recipient();
        recipient.setEmail("devansh@indiabizforsale.com");
        recipient.setTemplateData(templateData);
        Recipient recipient1 = new Recipient();
        recipient1.setEmail("devansh@indiabizforsale.com");
        recipient1.setTemplateData(templateData1);
        to.add(0, recipient);
        to.add(1,recipient1);
        payLoad.setTo(to);
        payLoad.setTemplateId("MyTemplate1");
        payLoad.setFrom("raj@indiabizforsale.com");
        payLoad.setFromName("Raj");
        Assert.assertTrue(emailSender.checkEmailCount(payLoad));

    }
}
