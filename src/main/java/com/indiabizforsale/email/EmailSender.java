package com.indiabizforsale.email;

import com.amazonaws.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simpleemail.model.*;


import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class EmailSender {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmailSender.class);

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

        if (payLoad.getCount() < 2)
            sendEmail(payLoad);
        else
            sendBulkEmail(payLoad);
        return true;
    }


    private void credentialCheck(ProfileCredentialsProvider profileCredentialsProvider) {

        try {
            profileCredentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(

                    "Cannot load the credentials from the credential profiles file. " +

                            "Please make sure that your credentials file is at the correct " +

                            "location (~/.aws/credentials), and is in valid format.",

                    e);
        }
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
                ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider();
                credentialCheck(profileCredentialsProvider);
                AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                        .withCredentials(profileCredentialsProvider).withRegion("us-west-2").build();

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
                    logger.info("Entered else");
                    logger.info("{}",c);
                    sendBulkTemplatedEmailRequest.setDestinations(c);
                    sendBulkTemplatedEmailRequest.setDefaultTemplateData("{}");
                    try {

                        logger.info("Attempting to send bulk email through Amazon SES by using the AWS SDK for Java...");

                        ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider();
                        credentialCheck(profileCredentialsProvider);
                        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                                .withCredentials(profileCredentialsProvider).withRegion("us-west-2").build();

                        SendBulkTemplatedEmailResult sendBulkTemplatedEmailResult = client.sendBulkTemplatedEmail(sendBulkTemplatedEmailRequest);
                        logger.info("{}",sendBulkTemplatedEmailResult.getStatus());
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
