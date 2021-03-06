package com.indiabizforsale.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendBulkTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.indiabizforsale.email.model.Attachments;
import com.indiabizforsale.email.model.PayLoad;
import com.indiabizforsale.email.model.Recipient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailSenderTest {
    private final Logger logger = LoggerFactory.getLogger(EmailSenderTest.class);
    @Mock
    private AmazonSimpleEmailServiceClient emailServiceClient;
    @Mock
    private AmazonSimpleEmailService amazonSimpleEmailService;
    private EmailSender emailSender;

    @Before
    public void setup() {
        emailSender = new EmailSender(emailServiceClient);
        when(emailServiceClient.getAmazonSimpleEmailService()).thenReturn(amazonSimpleEmailService);
    }

    @Test
    public void sendSingleEmail() throws IOException {
        PayLoad payLoad = new PayLoad();
        ArrayList<Recipient> to = new ArrayList<>();
        Recipient recipient = new Recipient();
        recipient.setEmail("devansh@indiabizforsale.com");
        recipient.setTemplateData(getTemplateData());
        to.add(recipient);
        payLoad.setTo(to);
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Devansh");
        payLoad.setTemplateName("MyTemplate1");
        payLoad.setConfigSet("Config1");
        ArrayList<Attachments> attachment = new ArrayList<>();
        Attachments attachmentData = new Attachments();
        attachmentData.setName("abc.pdf");
        attachmentData.setLink("link1");
        attachment.add(attachmentData);
        payLoad.setAttachments(attachment);
        logger.info("{}", payLoad);
        try {
            emailSender.sendEmail(payLoad);
            verify(amazonSimpleEmailService, times(1)).sendTemplatedEmail(any(SendTemplatedEmailRequest.class));
        } catch (Exception e) {
            logger.info("Exception:", e);
        }
    }

    private Map<String, Object> getTemplateData() {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("name", "Dev");
        return templateData;
    }

    private ArrayList<Recipient> getBulkEmailData() {
        ArrayList<Recipient> to = new ArrayList<>();
        Map<String, Object> templateData;
        Recipient recipient;
        for (int i = 0; i < 25; i++) {
            templateData = new HashMap<>();
            recipient = new Recipient();
            recipient.setEmail("Success@simulator.amazonses.com");
            templateData.put("name", "Success" + i + "");
            recipient.setTemplateData(templateData);
            to.add(i, recipient);
        }
        return to;
    }

    @Test
    public void sendBulkEmail() throws IOException {
        PayLoad payLoad = new PayLoad();
        payLoad.setTo(getBulkEmailData());
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Dev");
        payLoad.setTemplateName("MyTemplate1");
        payLoad.setConfigSet("config1");
        logger.info("{}", payLoad.toString());
        try {
            emailSender.sendEmail(payLoad);
            verify(amazonSimpleEmailService, times(2)).sendBulkTemplatedEmail(any(SendBulkTemplatedEmailRequest.class));
        } catch (Exception e) {
            logger.info("Exception:", e);
        }

    }

    @Test
    public void sendSingleFormattedEmail() throws IOException {
        PayLoad payLoad = new PayLoad();
        ArrayList<Recipient> to = new ArrayList<>();
        Recipient recipient = new Recipient();
        recipient.setEmail("devansh@indiabizforsale.com");
        to.add(recipient);
        payLoad.setTo(to);
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Devansh");
        payLoad.setSubject("Hello");
        payLoad.setBodyText("Hi Dev . How are you ?");
        payLoad.setBodyHtml("<p> Hi Dev . How are you ? </p>");
        payLoad.setConfigSet("Config1");
        try {
            emailSender.sendEmail(payLoad);
            verify(amazonSimpleEmailService, times(1)).sendEmail(any(SendEmailRequest.class));
        } catch (Exception e) {
            logger.info("Exception:", e);
        }
    }

    @Test
    public void sendBulkFormattedEmail() throws IOException {
        PayLoad payLoad = new PayLoad();
        payLoad.setTo(getBulkEmailData());
        payLoad.setFrom("devansh@indiabizforsale.com");
        payLoad.setFromName("Devansh");
        payLoad.setSubject("Hello");
        payLoad.setBodyText("Hi ${name} . How are you ?");
        payLoad.setBodyHtml("<p> Hi ${name} . It is from ${fromName}.</p>");
        payLoad.setConfigSet("Config1");
        try {
            emailSender.sendEmail(payLoad);
            verify(amazonSimpleEmailService, times(25)).sendEmail(any(SendEmailRequest.class));
        } catch (Exception e) {
            logger.info("Exception:", e);
        }
    }

    @Test
    public void getTemplatedMessageTest() {
        PayLoad payLoad = new PayLoad();
        payLoad.setBodyText("Hi ${name}.");
        Assert.assertEquals("Hi Dev.", emailSender.getTemplatedMessage(getTemplateData(), payLoad.getBodyText()));
    }


}
