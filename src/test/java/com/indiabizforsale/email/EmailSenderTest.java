package com.indiabizforsale.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EmailSenderTest {

    private EmailSender emailSender = new EmailSender();

    @Test
    public void sendSingleEmail() throws JsonProcessingException {

        new ConfigurationService().setEmailCredentials();
        PayLoad payLoad = new PayLoad();
        ArrayList<Recipient> to = new ArrayList<>();
        Map<String, String> templateData = new HashMap<>();
        Recipient recipient = new Recipient();
        templateData.put("name", "Dev");
        assertEquals(true, templateData.containsValue("Dev"));
        recipient.setEmail("devansh@indiabizforsale.com");
        recipient.setTemplateData(templateData);
        assertEquals("devansh@indiabizforsale.com", recipient.getRawEmail());
        assertEquals("{\"name\":\"Dev\"}", recipient.getTemplateDataJson());
        assertEquals(true, to.add(recipient));
        payLoad.setTo(to);
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Devansh");
        payLoad.setTemplateId("MyTemplate1");
        assertEquals("Dev <devansh@indiabizforsale.com>", payLoad.getTo().get(0).getEmail());
        assertEquals("devansh@indiabizforsale.com", payLoad.getRawFrom());
        assertEquals("Devansh", payLoad.getFromName());
        assertEquals("MyTemplate1", payLoad.getTemplateId());
        try {
            emailSender.sendEmail(payLoad);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void sendBulkEmail() {

    }

    @Test
    public void sendSingleFormattedEmail() {

    }

    @Test
    public void sendBulkFormattedEmail() {

    }


}
