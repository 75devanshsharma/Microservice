package com.indiabizforsale.email;
import com.amazonaws.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailResult;


import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

import java.util.Collection;
import java.util.Iterator;


public class EmailSender {

    /* public void mailJunction( Object to)
     {
      //if count of to is 1 then call the function sendEmail
         // else call sendBulkEmail function
     } */
    public void sendEmail(String to, String from, String name)
    {
//
//        Destination d = new Destination();
//        d.setToAddresses();

        SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();

        sendTemplatedEmailRequest.setDestination(new Destination().withToAddresses(to));
        sendTemplatedEmailRequest.setSource(from);
        sendTemplatedEmailRequest.setTemplate("MyTemplate");
        sendTemplatedEmailRequest.setTemplateData("{}");

        try {

            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

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

            SendTemplatedEmailResult sr =  client.sendTemplatedEmail(sendTemplatedEmailRequest);
            System.out.println(sr.getMessageId());
            System.out.println("Email sent!");


        } catch (Exception ex) {

            System.out.println("The email was not sent.");

            System.out.println("Error message: " + ex.getMessage());

        }
    }
}