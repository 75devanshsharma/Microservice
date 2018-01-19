package com.indiabizforsale.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.CreateTemplateRequest;
import com.amazonaws.services.simpleemail.model.DeleteTemplateRequest;
import com.amazonaws.services.simpleemail.model.ListTemplatesRequest;
import com.amazonaws.services.simpleemail.model.UpdateTemplateRequest;
import com.indiabizforsale.email.model.PayLoad;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AmazonEmailTemplateTest {

    @Mock
    AmazonSimpleEmailService amazonSimpleEmailService;
    @Mock
    private AmazonSimpleEmailServiceClient amazonSimpleEmailServiceClient;
    private AmazonEmailTemplate amazonEmailTemplate;

    @Before
    public void setup() {
        amazonEmailTemplate = new AmazonEmailTemplate(amazonSimpleEmailServiceClient);
        when(amazonSimpleEmailServiceClient.getAmazonSimpleEmailService()).thenReturn(amazonSimpleEmailService);
    }

    @Test
    public void createTemplateTest() {
        PayLoad payLoad = new PayLoad();
        payLoad.setTemplateName("Demo");
        payLoad.setTemplateSubject("This is a Demo");
        payLoad.setTemplateText("Hiii {{name}}. This is Demo TEXT.");
        payLoad.setTemplateHtml("<p>Hiii {{name}}. This is Demo HTML. <p>");
        amazonEmailTemplate.createEmailTemplate(payLoad);
        verify(amazonSimpleEmailService, times(1)).createTemplate(any(CreateTemplateRequest.class));
    }

    @Test
    public void viewTemplateTest() {
        PayLoad payLoad = new PayLoad();
        payLoad.setMaxTemplate(10);
        amazonEmailTemplate.viewEmailTemplates(payLoad);
        verify(amazonSimpleEmailService, times(1)).listTemplates(any(ListTemplatesRequest.class));
    }

    @Test
    public void deleteTemplateTest() {
        PayLoad payLoad = new PayLoad();
        payLoad.setTemplateName("Demo19");
        amazonEmailTemplate.deleteTemplate(payLoad);
        verify(amazonSimpleEmailService, times(1)).deleteTemplate(any(DeleteTemplateRequest.class));
    }

    @Test
    public void updateTemplateTest() {
        PayLoad payLoad = new PayLoad();
        payLoad.setTemplateName("Demo19");
        payLoad.setTemplateSubject("Hello World.");
        payLoad.setTemplateText(" Hello {{name}}.");
        payLoad.setTemplateHtml("<p> Hello {{name}}. <p>");
        amazonEmailTemplate.updateEmailTemplate(payLoad);
        verify(amazonSimpleEmailService, times(1)).updateTemplate(any(UpdateTemplateRequest.class));
    }
}
