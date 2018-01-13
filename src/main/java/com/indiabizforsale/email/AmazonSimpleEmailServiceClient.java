package com.indiabizforsale.email;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.slf4j.LoggerFactory;

public class AmazonSimpleEmailServiceClient {
    private final String awsAccessKey = System.getProperty("AwsAccessKey");
    private final String awsSecretKey = System.getProperty("AwsSecretKey");
    private static final String WITHREGION = "us-west-2";



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
