package com.indiabizforsale.email;

import com.amazonaws.services.simpleemail.model.SetIdentityFeedbackForwardingEnabledRequest;
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
//        Map<String,String> templateData1 = new HashMap<>();
        templateData.put("name", "Amazon");
//        templateData1.put("name", "Deva");
        Recipient recipient = new Recipient();
        recipient.setEmail("bounce@simulator.amazonses.com");
        recipient.setTemplateData(templateData);
//        Recipient recipient1 = new Recipient();
//        recipient1.setEmail("devansh@indiabizforsale.com");
//        recipient1.setTemplateData(templateData1);
        to.add(0, recipient);
//        to.add(1,recipient1);
        payLoad.setTo(to);
        payLoad.setTemplateId("MyTemplate1");
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Dev");
        Assert.assertTrue(emailSender.checkEmailCount(payLoad));

    }

    @Test
    public void bulkemailTest() throws IOException {
        EmailSender emailSender = new EmailSender();
        PayLoad payLoad = new PayLoad();
        ArrayList<Recipient> to = new ArrayList<>();
        Map<String, String> templateData = new HashMap<>();
        Map<String,String> templateData1 = new HashMap<>();
        templateData.put("name", "Amazon");
        templateData1.put("name", "Amazon1");
        Recipient recipient = new Recipient();
        recipient.setEmail("success@simulator.amazonses.com");
        recipient.setTemplateData(templateData);
        Recipient recipient1 = new Recipient();
        recipient1.setEmail("success@simulator.amazonses.com");
        recipient1.setTemplateData(templateData1);
        to.add(0, recipient);
        to.add(1,recipient1);
        payLoad.setTo(to);
        payLoad.setTemplateId("MyTemplate1");
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Dev");
        Assert.assertTrue(emailSender.checkEmailCount(payLoad));

    }
}
