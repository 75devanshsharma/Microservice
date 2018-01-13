package com.indiabizforsale.email;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

public class AmazonSimpleEmailServiceClient {
    private static final String WITHREGION = "us-west-2";
    private final String awsAccessKey = System.getProperty("AwsAccessKey");
    private final String awsSecretKey = System.getProperty("AwsSecretKey");

    public AmazonSimpleEmailService getAmazonSimpleEmailService() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).withRegion(WITHREGION).build();
    }

    @Override
    public String toString() {
        return "AmazonSimpleEmailServiceClient{" +
                "awsAccessKey='" + awsAccessKey + '\'' +
                ", awsSecretKey='" + awsSecretKey + '\'' +
                '}';
    }
}
