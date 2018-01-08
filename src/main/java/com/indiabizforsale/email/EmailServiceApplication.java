package com.indiabizforsale.email;

import com.google.cloud.pubsub.v1.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EmailServiceApplication {

    //To run all the email services.
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceApplication.class);
    public static String AwsAccessKeyId;
    public static String AccessKeySecret;
    private String runMode;

    public static void main(String[] args) {

        ConfigurationService configurationService = new ConfigurationService();

        configurationService.getCredentials();
        AwsAccessKeyId = configurationService.getAccessKey();
        AccessKeySecret = configurationService.getSecretKey();
        if (args.length == 1) {
            logger.debug("Starting listener on {} ", args[0]);
            new EmailServiceApplication().listenAndProcess(args[0]);
        } else {
            logger.error("Provide subscription name and mode : java -jar app.jar [subscriptionName]");
        }
    }

    private void listenAndProcess(String subscription) {
        assignRunMode();
        logger.info("Running Application in {}", runMode);
        Subscriber subscriber = null;
        PubSubSubscriber pubSubSubscriber = new PubSubSubscriber(subscription, runMode);
        try {
            subscriber = pubSubSubscriber.getSubscriber();
            pubSubSubscriber.startSubscriber(subscriber);
            synchronized (this) {
                wait();
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        } finally {
            if (subscriber != null) {
                subscriber.stopAsync().awaitTerminated();
            }
        }
    }

    private void assignRunMode() {
        try {
            runMode = System.getenv("SERVICE_MODE");
        } catch (NullPointerException | SecurityException e) {
            logger.warn("Runtime Environment not set, running in test mode", e);
            runMode = "test";
        }
        if (runMode.isEmpty()) {
            runMode = "test";
        }
    }
}



