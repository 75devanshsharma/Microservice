package com.indiabizforsale.email;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmailSenderTest {

    @Before
    public void setEmailCredential() {
        new ConfigurationService().setEmailCredentials();
    }

    @Test
    public void emailTest() throws IOException {
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

    @Test
    public void sendFormattedEmailTest() throws IOException {
        EmailSender emailSender = new EmailSender();
        PayLoad payLoad = new PayLoad();
        ArrayList<Recipient> to = new ArrayList<>();
        Recipient recipient = new Recipient();
        recipient.setEmail("devansh@indiabizforsale.com");
        to.add(0, recipient);
        payLoad.setTo(to);
        payLoad.setSubject("Hello User");
        payLoad.setBodyHtml("This message body contains HTML formatting. Links like......");
        payLoad.setBodyText("Hi user. Hope you are doing good. This is body text.");
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Dev");
        Assert.assertTrue(emailSender.checkEmailCount(payLoad));
    }

    @Test
    public void sendBulkFormattedEmailTest() throws IOException {
        EmailSender emailSender = new EmailSender();
        PayLoad payLoad = new PayLoad();
        Recipient recipient;
        ArrayList<Recipient> to = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            recipient = new Recipient();
            recipient.setEmail("success@simulator.amazonses.com");
            to.add(i, recipient);
        }
        payLoad.setTo(to);
        payLoad.setSubject("Hello User");
        payLoad.setBodyHtml("This message body contains HTML formatting. Links like......");
        payLoad.setBodyText("Hi user. Hope you are doing good. This is body text.");
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Dev");
        Assert.assertTrue(emailSender.checkEmailCount(payLoad));
    }

    @After
    public void removeEmailCredentials() {
        System.clearProperty("AwsAccessKey");
        System.clearProperty("AwsSecretKey");
    }
}
