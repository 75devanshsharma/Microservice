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
        System.setProperty("AwsAccessKey",new ConfigurationService().getAccessKey());
        System.setProperty("AwsSecretKey", new ConfigurationService().getSecretKey());
        EmailSender emailSender = new EmailSender();
        PayLoad payLoad = new PayLoad();
        ArrayList<Recipient> to = new ArrayList<>();
        Map<String, String> templateData = new HashMap<>();
        templateData.put("name", "Amazon");
        Recipient recipient = new Recipient();
        recipient.setEmail("bounce@simulator.amazonses.com");
        recipient.setTemplateData(templateData);
        to.add(0, recipient);
        payLoad.setTo(to);
        payLoad.setTemplateId("MyTemplate1");
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Dev");
        Assert.assertTrue(emailSender.checkEmailCount(payLoad));

    }

    @Test
    public void bulkEmailTest() throws IOException {
        System.setProperty("AwsAccessKey",new ConfigurationService().getAccessKey());
        System.setProperty("AwsSecretKey", new ConfigurationService().getSecretKey());
        EmailSender emailSender = new EmailSender();
        PayLoad payLoad = new PayLoad();
        Recipient recipient;
        ArrayList<Recipient> to = new ArrayList<>();
        Map<String, String> templateData;
        for (int i = 0; i < 25; i++) {
            templateData = new HashMap<>();
            recipient = new Recipient();
            templateData.put(";name", "Amazon");
            recipient.setEmail("success@simulator.amazonses.com");
            recipient.setTemplateData(templateData);
            to.add(i, recipient);
        }
        payLoad.setTo(to);
        payLoad.setTemplateId("MyTemplate1");
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Dev");
        Assert.assertTrue(emailSender.checkEmailCount(payLoad));

    }
}
