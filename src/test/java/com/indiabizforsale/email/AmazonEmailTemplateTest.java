package com.indiabizforsale.email;

import com.indiabizforsale.email.model.PayLoad;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class AmazonEmailTemplateTest {

    private AmazonSimpleEmailServiceClient client;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AmazonEmailTemplateTest.class);
    private AmazonEmailTemplate amazonEmailTemplate;

    @Before
    public void setup() {
        new ConfigurationService().setEmailCredentials();
        client = new AmazonSimpleEmailServiceClient();
        amazonEmailTemplate = new AmazonEmailTemplate(client);
    }

    @Test
    public void createTemplateTest() {
        for (int i =0; i < 5; i++) {
            PayLoad payLoad = new PayLoad();
            payLoad.setTemplateName("Demo" + i);
            payLoad.setTemplateSubject("This is a Demo" + i);
            payLoad.setTemplateText("Hiii {{name}}. This is Demo" + i + "TEXT.");
            payLoad.setTemplateHtml("<p>Hiii {{name}}. This is Demo" + i + "HTML. <p>");
            amazonEmailTemplate.createEmailTemplate(payLoad);
        }
    }

    @Test
    public void viewTemplateTest() {
        PayLoad payLoad = new PayLoad();
        payLoad.setMaxTemplate(10);
        amazonEmailTemplate.viewEmailTemplates(payLoad);
    }

    @Test
    public void deleteTemplateTest() {
        PayLoad payLoad = new PayLoad();
        payLoad.setTemplateName("Demo19");
        amazonEmailTemplate.deleteTemplate(payLoad);
    }

    @Test
    public void updateTemplateTest()
    {
        PayLoad payLoad = new PayLoad();
        payLoad.setTemplateName("Demo19");
        payLoad.setTemplateSubject("Hello World.");
        payLoad.setTemplateText(" Hello {{name}}.");
        payLoad.setTemplateHtml("<p> Hello {{name}}. <p>");
        amazonEmailTemplate.updateEmailTemplate(payLoad);
    }
}
