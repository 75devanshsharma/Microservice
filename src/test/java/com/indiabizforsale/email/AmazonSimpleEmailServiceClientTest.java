package com.indiabizforsale.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AmazonSimpleEmailServiceClientTest {

    private AmazonSimpleEmailServiceClient emailServiceClient;

    @Test
    public void getAmazonSimpleEmailService() {
        new ConfigurationService().setEmailCredentials();
        emailServiceClient = new AmazonSimpleEmailServiceClient();
        AmazonSimpleEmailService amazonSimpleEmailService = emailServiceClient.getAmazonSimpleEmailService();
        assertNotNull(amazonSimpleEmailService);
    }
}