package com.indiabizforsale.email;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class EmailSender {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmailSender.class);


    private String awsAccessKey = System.getProperty("AwsAccessKey");
    private String awsSecretKey = System.getProperty("AwsSecretKey");


    /**
     * <p>To count the number of recipient in the payload.
     * If the count is 1 then sendEmail method is used for sending emails
     * otherwise, sendBulkEmail method for sending email to all.
     * </p>
     *
     * @param payLoad
     * @throws IOException
     */
    public boolean checkEmailCount(PayLoad payLoad) throws IOException {

        if (payLoad.getTemplateId() != null) {
            if (payLoad.getCount() < 2)
                sendEmail(payLoad);
            else
                sendBulkEmail(payLoad);
        } else if(payLoad.getCount()<2)
            sendFormattedEmail(payLoad);
        else
            sendBulkFormattedEmail(payLoad);
        return true;
    }


    /**
     * <p> This method is used to send email to a single recipient by setting the to,from,templateId
     * & template data. The email is sent using the Amazon SES service.</p>
     *
     * @param payLoad
     * @throws IOException
     */

    private void sendEmail(PayLoad payLoad) throws IOException {
        EmailValidationService emailValidationService = new EmailValidationService();

        if (emailValidationService.emailValidate(payLoad.getTo().get(0).getRawEmail())) {
            SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();
            sendTemplatedEmailRequest.setDestination(new Destination().withToAddresses(payLoad.getFirstEmail()));
            sendTemplatedEmailRequest.setSource(payLoad.getFrom());
            sendTemplatedEmailRequest.setTemplate(payLoad.getTemplateId());
            sendTemplatedEmailRequest.setTemplateData(payLoad.getFirstTemplate());

            try {

                logger.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
                BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
                logger.info("{}", basicAWSCredentials);
                AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).withRegion("us-west-2").build();

                SendTemplatedEmailResult sr = client.sendTemplatedEmail(sendTemplatedEmailRequest);
                logger.info(sr.getMessageId());
                logger.info("Email sent!");

            } catch (Exception ex) {

                logger.warn("The email was not sent.");
                logger.warn("Error message: " + ex.getMessage());
            }
        } else
            logger.warn("Email format entered is incorrect. Please Check the email.");
    }

    /**
     * <p> This method is used for sending bulk emails. More than one recipient receive the message by passing
     * the destination address.
     * The destination is stored in the collection of arrayList.
     * The batching is done to send 20 emails max per call to Amazon SES.
     * </p>
     *
     * @param payLoad
     * @throws JsonProcessingException
     */

    private void sendBulkEmail(PayLoad payLoad) throws JsonProcessingException {
        ArrayList<Recipient> arrayList = payLoad.getTo();
        int count = 0;

        SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest = new SendBulkTemplatedEmailRequest();
        sendBulkTemplatedEmailRequest.setSource(payLoad.getFrom());
        sendBulkTemplatedEmailRequest.setTemplate(payLoad.getTemplateId());
        BulkEmailDestination bulkEmailDestination;
        Collection<BulkEmailDestination> c = new ArrayList<>();
        Destination destination;
        EmailValidationService emailValidationService = new EmailValidationService();
        Iterator itr = arrayList.iterator();

        while (itr.hasNext()) {
            Recipient next = (Recipient) itr.next();
            if (emailValidationService.emailValidate(next.getRawEmail())) {
                logger.info("Entered if");
                bulkEmailDestination = new BulkEmailDestination();
                destination = new Destination();
                destination.withToAddresses(next.getEmail());
                bulkEmailDestination.setDestination(destination);
                bulkEmailDestination.setReplacementTemplateData(next.getTemplateDataJson());
                c.add(bulkEmailDestination);
                count++;
                itr.remove();
                if ((count < 20) && (itr.hasNext()))
                    continue;
                else {
                    logger.info("Count is {}", count);
                    logger.info("Entered else");
                    logger.info("{}", c);
                    sendBulkTemplatedEmailRequest.setDestinations(c);
                    sendBulkTemplatedEmailRequest.setDefaultTemplateData("{}");
                    try {

                        logger.info("Attempting to send bulk email through Amazon SES by using the AWS SDK for Java...");

                        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

                        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).withRegion("us-west-2").build();

                        SendBulkTemplatedEmailResult sendBulkTemplatedEmailResult = client.sendBulkTemplatedEmail(sendBulkTemplatedEmailRequest);
                        logger.info("{}", sendBulkTemplatedEmailResult.getStatus());
                        logger.info("Email sent!");
                    } catch (Exception ex) {

                        logger.warn("The email was not sent.");
                        logger.warn("Error message: " + ex.getMessage());
                    }
                    count = 0;
                }
            } else {
                logger.debug("Wrong email format. Email ignored.");
            }
        }
        logger.info("Out of while loop.");
    }


    /**
     * <h4> This method is used for sending formatted email to single recipient.</h4>
     *
     * @param payLoad
     */
    private void sendFormattedEmail(PayLoad payLoad) {
        EmailValidationService emailValidationService = new EmailValidationService();
        logger.info("Entered sendFormattedEmail");

        if (emailValidationService.emailValidate(payLoad.getTo().get(0).getRawEmail())) {
            SendEmailRequest sendEmailRequest = new SendEmailRequest();
            sendEmailRequest.setDestination(new Destination().withToAddresses(payLoad.getTo().get(0).getRawEmail()));
            sendEmailRequest.setSource(payLoad.getFrom());
            sendEmailRequest.setMessage(new Message().withSubject(new Content().withData(payLoad.getSubject())
                    .withCharset("UTF-8")).withBody(new Body().withText(new Content().withData(payLoad.getBodyText())
                    .withCharset("UTF-8")).withHtml(new Content().withData(payLoad.getBodyText()).withCharset("UTF-8"))));

            logger.info("{}", sendEmailRequest);
            try {
                logger.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

                BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

                AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().
                        withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).withRegion("us-west-2").build();

                SendEmailResult sendEmailResult = client.sendEmail(sendEmailRequest);

                logger.info("Email sent.");
                logger.info("{}", sendEmailResult.getMessageId());
            } catch (Exception e) {
                logger.warn("The email was not sent.");
                logger.warn("Error message: " + e.getMessage());
            }
        } else {
            logger.info("Email-format is invalid. Please check the format.");
        }
    }

    /**
     * <p> This method is used for sending formatted email to many number of recipients.
     * The destinations are stored in collection and are then passed to destination object.
     * Batching of 20 emails per request is done.</p>
     *
     * @param payLoad
     */
    private void sendBulkFormattedEmail(PayLoad payLoad)
    {
        logger.info("Entered sendBulkFormattedEmail");
        ArrayList<Recipient> arrayList = payLoad.getTo();
        int count = 0;
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.withSource(payLoad.getFrom());
        sendEmailRequest.setMessage(new Message().withSubject(new Content().withData(payLoad.getSubject())
                .withCharset("UTF-8")).withBody(new Body().withText(new Content().withData(payLoad.getBodyText())
                .withCharset("UTF-8")).withHtml(new Content().withData(payLoad.getBodyText()).withCharset("UTF-8"))));
        Collection<String> c;
        Destination destination = new Destination();
        EmailValidationService emailValidationService = new EmailValidationService();
        Iterator itr = arrayList.iterator();

        while (itr.hasNext()) {
            Recipient next = (Recipient) itr.next();
            if (emailValidationService.emailValidate(next.getRawEmail())) {
                logger.info("Entered if");
                c = new ArrayList<>();
                c.add(next.getRawEmail());
                count++;
                itr.remove();
                if ((count < 20) && (itr.hasNext()))
                    continue;
                else {
                    logger.info("Count is {}", count);
                    logger.info("Entered else");
                    logger.info("{}", c);
                    destination.withToAddresses(c);
                    sendEmailRequest.withDestination(destination);

                    try {

                        logger.info("Attempting to send bulk email through Amazon SES by using the AWS SDK for Java...");

                        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

                        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).withRegion("us-west-2").build();

                       SendEmailResult sendEmailResult = client.sendEmail(sendEmailRequest);
                        logger.info("{}", sendEmailResult.getMessageId());
                        logger.info("Email sent!");
                    } catch (Exception ex) {

                        logger.warn("The email was not sent.");
                        logger.warn("Error message: " + ex.getMessage());
                    }
                    count = 0;
                }
            } else {
                logger.debug("Wrong email format. Email ignored.");
            }
        }
        logger.info("Out of while loop.");
    }

}
