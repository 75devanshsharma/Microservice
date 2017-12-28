package com.indiabizforsale.email;
import com.amazonaws.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simpleemail.model.*;


import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;


public class EmailSender {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmailSender.class);

    /* public void mailJunction( Object to)
     {
      //if count of to is 1 then call the function sendEmail
         // else call sendBulkEmail function
     } */

    public void checkEmailCount( PayLoad payLoad)
    {
        //check the count of To emails.
        //redirect the control to the methods accordingly.
    }

    public void sendEmail(String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        EmailValidationService emailValidationService = new EmailValidationService();
        PayLoad payLoad = mapper.readValue(message, PayLoad.class);

        if(emailValidationService.emailValidate(payLoad.getTo().get(0).getEmail()))
        {
            SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();
            sendTemplatedEmailRequest.setDestination(new Destination().withToAddresses(payLoad.getFirstEmail()));
            sendTemplatedEmailRequest.setSource(payLoad.getFrom());
            sendTemplatedEmailRequest.setTemplate(payLoad.getTemplateId());
            sendTemplatedEmailRequest.setTemplateData(payLoad.getFirstTemplate());

            try {

                logger.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

                ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider();
                try {
                    profileCredentialsProvider.getCredentials();
                } catch (Exception e) {
                    throw new AmazonClientException(

                            "Cannot load the credentials from the credential profiles file. " +

                                    "Please make sure that your credentials file is at the correct " +

                                    "location (~/.aws/credentials), and is in valid format.",

                            e);
                }
                AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                        .withCredentials(profileCredentialsProvider).withRegion("us-west-2").build();

                SendTemplatedEmailResult sr = client.sendTemplatedEmail(sendTemplatedEmailRequest);
                logger.info(sr.getMessageId());
                logger.info("Email sent!");

            } catch (Exception ex) {

                logger.warn("The email was not sent.");
                logger.warn("Error message: " + ex.getMessage());
            }
        }
        else
            logger.warn("Email format entered is incorrect. Please Check the email.");
    }

//         public boolean sendBulkEmail(PayLoad payLoad) throws JsonProcessingException
//         {
//            SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest = new SendBulkTemplatedEmailRequest();
//            sendBulkTemplatedEmailRequest.setDestinations(payLoad.getAllEmails());
//            sendBulkTemplatedEmailRequest.setSource("raj@indiabizforsale.com");
//            sendBulkTemplatedEmailRequest.setTemplate("MyTemplate");
//            sendBulkTemplatedEmailRequest.setDefaultTemplateData("MyTemplate");
//        try {
//
//            logger.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
//
//            ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider();
//            try {
//                profileCredentialsProvider.getCredentials();
//            } catch (Exception e) {
//                throw new AmazonClientException(
//
//                        "Cannot load the credentials from the credential profiles file. " +
//
//                                "Please make sure that your credentials file is at the correct " +
//
//                                "location (~/.aws/credentials), and is in valid format.",
//
//                        e);
//            }
//            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
//                    .withCredentials(profileCredentialsProvider).withRegion("us-west-2").build();
//
//            SendBulkTemplatedEmailResult sendBulkTemplatedEmailResult = client.sendBulkTemplatedEmail(sendBulkTemplatedEmailRequest);
////            logger.info(sendBulkTemplatedEmailResult.getStatus());
//            logger.info("Email sent!");
//            return true;
//
//        } catch (Exception ex) {
//
//            logger.warn("The email was not sent.");
//            logger.warn("Error message: " + ex.getMessage());
//        }
//        return false;
//    }

}