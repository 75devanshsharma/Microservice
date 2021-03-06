package com.indiabizforsale.email;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.pubsub.v1.PubsubMessage;
import com.indiabizforsale.email.exceptions.EventParserException;
import com.indiabizforsale.email.model.PayLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageReceiverImpl implements com.google.cloud.pubsub.v1.MessageReceiver {
    private static final Logger logger = LoggerFactory.getLogger(MessageReceiverImpl.class);

    /**
     * <p>
     * This method receives message from Google cloud PubSub in the JSon form.
     * The messages are stored in payload object and are sent to sendEmail
     * method for sending the email.
     * Acknowledgment is given back to the PubSub.
     * </p>
     *
     * @param message
     * @param consumer
     */


    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        EmailSender emailSender = new EmailSender(new AmazonSimpleEmailServiceClient());
        ObjectMapper mapper = new ObjectMapper();

        String msg = message.getData().toStringUtf8();
        logger.info("Message {} ", msg);
        try {
            PayLoad payLoad = mapper.readValue(msg, PayLoad.class);
            emailSender.sendEmail(payLoad);
        } catch (EventParserException e) {
            logger.error("Exception", e);
            consumer.ack();
        } catch (IOException e) {
            logger.error("IOException", e);
            consumer.ack();
        }
        catch(Exception e){
            logger.error("Exception",e);
            consumer.ack();
        }
        consumer.ack();

    }
}
