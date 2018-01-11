package com.indiabizforsale.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmailSenderTest {


    @Mock
    private AmazonSimpleEmailServiceClient emailServiceClient;
    @Mock
    private AmazonSimpleEmailService amazonSimpleEmailService;
    private EmailSender emailSender;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        emailSender = new EmailSender(emailServiceClient);
        Mockito.when(emailServiceClient.getAmazonSimpleEmailService()).thenReturn(amazonSimpleEmailService);
    }

    @Test
    public void sendSingleEmail() {
        PayLoad payLoad = new PayLoad();
        ArrayList<Recipient> to = new ArrayList<>();
        Recipient recipient = new Recipient();
        recipient.setEmail("devansh@indiabizforsale.com");
        recipient.setTemplateData(getTemplateData());
        to.add(recipient);
        payLoad.setTo(to);
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Devansh");
        payLoad.setTemplateId("MyTemplate1");
        try {
            emailSender.sendEmail(payLoad);
            Mockito.verify(amazonSimpleEmailService, Mockito.times(1)).sendTemplatedEmail(Mockito.any(SendTemplatedEmailRequest.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getTemplateData() {
        Map<String, String> templateData = new HashMap<>();
        templateData.put("name", "Dev");
        return templateData;
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
