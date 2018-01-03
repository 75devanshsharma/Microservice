package com.indiabizforsale.email;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmailSenderTest {
    @Test
    public static void main(String[] args) {
        EmailSender emailSender = new EmailSender();
        PayLoad payLoad = new PayLoad();
        ArrayList<Recipient> to = new ArrayList<>();
        Map<String, String> templateData = new HashMap<>();
        templateData.put("name", "Dev");
        templateData.put("name", "Deva");
        Recipient recipient = new Recipient();
        recipient.setEmail("devansh@indiabizforsale.com");
        recipient.setTemplateData(templateData);
        to.add(0, recipient);
        payLoad.setTo(to);
        payLoad.setTemplateId("MyTemplate1");
        payLoad.setFrom("raj@indiabizforsale.com");
        payLoad.setFromName("Raj");
        try {
            emailSender.checkEmailCount(payLoad);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
