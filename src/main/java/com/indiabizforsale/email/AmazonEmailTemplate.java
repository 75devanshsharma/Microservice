package com.indiabizforsale.email;

import com.amazonaws.services.simpleemail.model.*;
import com.indiabizforsale.email.model.PayLoad;
import org.slf4j.LoggerFactory;

public class AmazonEmailTemplate {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AmazonEmailTemplate.class);
    private AmazonSimpleEmailServiceClient client;

    public AmazonEmailTemplate(AmazonSimpleEmailServiceClient client) {
        this.client = client;
        logger.info("{}", client);
    }

    public void createEmailTemplate(PayLoad payLoad) {
        logger.info("Entered createEmailTemplate");
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest();
        Template template = new Template();
        template.withTemplateName(payLoad.getTemplateName());
        template.withSubjectPart(payLoad.getTemplateSubject());
        template.withHtmlPart(payLoad.getTemplateHtml());
        template.withTextPart(payLoad.getTemplateText());
        createTemplateRequest.setTemplate(template);
        logger.info("{}", createTemplateRequest);

        try {
            logger.info("Attempting to create template through Amazon SES .....");
            CreateTemplateResult createTemplateResult = client.getAmazonSimpleEmailService().createTemplate(createTemplateRequest);
            logger.info("Template created successfully.");
            logger.info("{}", createTemplateResult.toString());
        } catch (Exception e) {
            logger.error("Could not create template..Error :", e);
        }

    }

    public void deleteTemplate(PayLoad payLoad) {
        logger.info("Entered into deleteTemplate");
        DeleteTemplateRequest deleteTemplateRequest = new DeleteTemplateRequest();

        deleteTemplateRequest.withTemplateName(payLoad.getTemplateName());
        logger.info("{}", deleteTemplateRequest);
        try {
            logger.info("Attempting to delete template through Amazon SES .....");
            DeleteTemplateResult deleteTemplateResult = client.getAmazonSimpleEmailService().deleteTemplate(deleteTemplateRequest);
            logger.info("Template Deleted Successfully.");
            logger.info("{}", deleteTemplateResult);
        } catch (Exception ex) {
            logger.error("Could not delete template...Error -", ex);
        }

    }

    public void viewEmailTemplates(PayLoad payLoad) {
        ListTemplatesRequest listTemplatesRequest = new ListTemplatesRequest();
        listTemplatesRequest.withMaxItems(payLoad.getMaxTemplate());

        try {
            logger.info("Attempting to view the list using Amazon SES...");
            ListTemplatesResult listTemplatesResult = client.getAmazonSimpleEmailService().listTemplates(listTemplatesRequest);
            logger.info("{}", listTemplatesResult.toString());
            while (listTemplatesResult.getNextToken() != null) {
                listTemplatesRequest.setNextToken(listTemplatesResult.getNextToken());
                listTemplatesResult = client.getAmazonSimpleEmailService().listTemplates(listTemplatesRequest);
                logger.info("{}", listTemplatesResult);
            }
        } catch (Exception ev) {
            logger.error("Cannot view the template..error -", ev);
        }
    }

    public void updateEmailTemplate(PayLoad payload) {
        UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest();
        Template template = new Template();
        template.setTemplateName(payload.getTemplateName());
        template.setSubjectPart(payload.getTemplateSubject());
        template.setTextPart(payload.getTemplateText());
        template.setHtmlPart(payload.getTemplateHtml());
        updateTemplateRequest.setTemplate(template);
        try {
            logger.info("Attempting to update the template using Amazon SES");
            UpdateTemplateResult updateTemplateResult = client.getAmazonSimpleEmailService().updateTemplate(updateTemplateRequest);
            logger.info("{}", updateTemplateResult.toString());
            logger.info("Template Updated");
        } catch (Exception eu) {
            logger.error("Cannot update template..Error :", eu);

        }
    }

}

