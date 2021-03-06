package com.indiabizforsale.email;

import com.google.api.gax.batching.FlowControlSettings;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.SubscriptionName;
import com.indiabizforsale.email.exceptions.SubscriberCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PubSubSubscriber {
    private static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();
    private static final Logger logger = LoggerFactory.getLogger(PubSubSubscriber.class);
    private String subscriptionId;

    PubSubSubscriber(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void startSubscriber(Subscriber subscriber) {
        subscriber.startAsync().awaitRunning();
    }

    /**
     * <p>
     * This method is used to control the rate at which a subscriber retrieves messages
     * by using the flow control features of the subscriber.
     * </p>
     *
     * @return subscriber information.
     */
    public Subscriber getSubscriber() {
        long maxMessageCount = 10L;
        logger.info("Init listening ");
        FlowControlSettings flowControlSettings =
                FlowControlSettings.newBuilder().setMaxOutstandingElementCount(maxMessageCount).build();
        SubscriptionName subscriptionName = SubscriptionName.of(PROJECT_ID, subscriptionId);
        Subscriber subscriber;

        try {
            subscriber = Subscriber.newBuilder(subscriptionName, new MessageReceiverImpl())
                    .setFlowControlSettings(flowControlSettings)
                    .build();
            subscriber.addListener(
                    new Subscriber.Listener() {
                        @Override
                        public void failed(Subscriber.State from, Throwable failure) {
                            logger.error("Exception", failure);
                        }
                    },
                    MoreExecutors.directExecutor());
            return subscriber;
        } catch (Exception e) {
            logger.error("Exception", e);
            throw new SubscriberCreationException(e);
        }
    }
}