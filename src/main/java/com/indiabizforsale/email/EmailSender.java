package com.indiabizforsale.email;

import com.amazonaws.services.simpleemail.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.indiabizforsale.email.model.PayLoad;
import com.indiabizforsale.email.model.Recipient;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


public class EmailSender extends RecursiveAction {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmailSender.class);
    private static final String WITHCHARSET = "UTF-8";
    private final static int TASK_LEN = 10;
    private AmazonSimpleEmailServiceClient client;
    private PayLoad payLoad;
    private int from;
    private int count;

    public EmailSender(AmazonSimpleEmailServiceClient client) {
        this.client = client;
        logger.info("{}", this.client);
    }

    public EmailSender(PayLoad payLoad,int from, int count, AmazonSimpleEmailServiceClient client1)
    {
        this.payLoad = payLoad;
        this.from =from;
        this.count = count;
        this.client = client1;
    }

    @Override
    protected void compute() {
        int len = count -from;
        if(len<TASK_LEN){
            sendBulkFormattedEmail(client,payLoad,from,count);
        }
        else
        {
            int mid = (from+count)>>>1;
            new EmailSender(payLoad,from,mid,client).fork();
            new EmailSender(payLoad,mid,count,client).fork();
        }
    }

    public void ParallelProcessing(PayLoad payLoad)
    {
        int count = payLoad.getToAddressCount();
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        forkJoinPool.invoke(new EmailSender(payLoad,0,count,client));
    }

    /**
     * <p>To count the number of recipient in the payload.
     * If the count is 1 then sendSingleEmail method is used for sending emails
     * otherwise, sendBulkEmail method for sending email to all.
     * </p>
     *
     * @param payLoad
     * @throws IOException
     */
    public void sendEmail(PayLoad payLoad) throws IOException {

        if (payLoad.getTemplateId() != null) {
            if (payLoad.getToAddressCount() < 2)
                sendSingleEmail(payLoad);
            else
                sendBulkEmail(payLoad);
        } else if (payLoad.getToAddressCount() < 2)
            sendSingleFormattedEmail(payLoad);
        else
            ParallelProcessing(payLoad);
    }


    /**
     * <p> This method is used to send email to a single recipient by setting the to,from,templateId
     * & template data. The email is sent using the Amazon SES service.</p>
     *
     * @param payLoad
     * @throws IOException
     */

    private void sendSingleEmail(PayLoad payLoad) throws IOException {
        EmailValidationService emailValidationService = new EmailValidationService();

        if (emailValidationService.isValid(payLoad.getTo().get(0).getRawEmail())) {
            SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();
            sendTemplatedEmailRequest.setDestination(new Destination().withToAddresses(payLoad.getFirstEmail()));
            sendTemplatedEmailRequest.setSource(payLoad.getFrom());
            sendTemplatedEmailRequest.setTemplate(payLoad.getTemplateId());
            sendTemplatedEmailRequest.setTemplateData(payLoad.getFirstTemplate());

            try {
                logger.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
                SendTemplatedEmailResult sr = client.getAmazonSimpleEmailService().sendTemplatedEmail(sendTemplatedEmailRequest);
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
        ArrayList<Recipient> recipients = payLoad.getTo();
        SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest = new SendBulkTemplatedEmailRequest();
        sendBulkTemplatedEmailRequest.setSource(payLoad.getFrom());
        sendBulkTemplatedEmailRequest.setTemplate(payLoad.getTemplateId());
        Collection<BulkEmailDestination> bulkEmailDestinations = new ArrayList<>();
        EmailValidationService emailValidationService = new EmailValidationService();
        Iterator itr = recipients.iterator();

        int count = 0;
        while (itr.hasNext()) {
            Recipient recipient = (Recipient) itr.next();
            if (emailValidationService.isValid(recipient.getRawEmail())) {
                logger.info("Entered if");
                BulkEmailDestination bulkEmailDestination = new BulkEmailDestination();
                Destination destination = new Destination();
                destination.withToAddresses(recipient.getEmail());
                bulkEmailDestination.setDestination(destination);
                bulkEmailDestination.setReplacementTemplateData(recipient.getTemplateDataJson());
                bulkEmailDestinations.add(bulkEmailDestination);
                count++;
                if (count >= 20 || !itr.hasNext()) {
                    logger.info("Count is {}", count);
                    logger.info("Entered else");
                    logger.info("{}", bulkEmailDestinations);
                    sendBulkTemplatedEmailRequest.setDestinations(bulkEmailDestinations);
                    sendBulkTemplatedEmailRequest.setDefaultTemplateData("{}");
                    try {
                        logger.info("Attempting to send bulk emails through Amazon SES by using the AWS SDK for Java...");
                        SendBulkTemplatedEmailResult sendBulkTemplatedEmailResult = client.getAmazonSimpleEmailService().sendBulkTemplatedEmail(sendBulkTemplatedEmailRequest);
                        logger.info("{}", sendBulkTemplatedEmailResult.getStatus());
                        logger.info("Email sent..");
                    } catch (Exception ex) {
                        logger.error("The email was not sent!!!", ex);
                    }
                    count = 0;
                    bulkEmailDestinations.clear();
                }
            } else {
                logger.debug("Wrong email format. Email is ignored ", recipient.getRawEmail());
            }
        }
        logger.info("Out of while loop.");
    }


    /**
     * <h4> This method is used for sending formatted email to single recipient.</h4>
     *
     * @param payLoad
     */
    private void sendSingleFormattedEmail(PayLoad payLoad) {
        EmailValidationService emailValidationService = new EmailValidationService();
        logger.info("Entered sendSingleFormattedEmail");

        if (emailValidationService.isValid(payLoad.getTo().get(0).getRawEmail())) {
            SendEmailRequest sendEmailRequest = new SendEmailRequest();
            sendEmailRequest.setDestination(new Destination().withToAddresses(payLoad.getTo().get(0).getRawEmail()));
            sendEmailRequest.setSource(payLoad.getFrom());
            sendEmailRequest.setMessage(new Message().withSubject(new Content().withData(payLoad.getSubject())
                    .withCharset(WITHCHARSET)).withBody(new Body().withText(new Content().
                    withData(payLoad.getBodyText())
                    .withCharset(WITHCHARSET)).withHtml(new Content().withData(payLoad.getBodyText()).withCharset(WITHCHARSET))));

            logger.info("{}", sendEmailRequest);
            try {
                logger.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java....");

                SendEmailResult sendEmailResult = client.getAmazonSimpleEmailService().sendEmail(sendEmailRequest);

                logger.info("Email sent..");
                logger.info("{}", sendEmailResult.getMessageId());
            } catch (Exception e) {
                logger.error("The email was not sent!", e);
            }
        } else {
            logger.info("Email-format is invalid. Please check the format.");
        }
    }

    /**
     * <p> This method is used for sending formatted email to many number of recipients.
     * Every recipient receives message with their own template data.</p>
     *
     * @param payLoad
     */
    private void sendBulkFormattedEmail(AmazonSimpleEmailServiceClient client1, PayLoad payLoad, int from, int count) {
        logger.info("Entered sendBulkFormattedEmail");
        EmailValidationService emailValidationService = new EmailValidationService();
        for (int i = from; i < count; i++) {
            Map<String, String> templateData = payLoad.getTo().get(i).getTemplateData();
            if (emailValidationService.isValid(payLoad.getTo().get(i).getRawEmail())) {
                SendEmailRequest sendEmailRequest = new SendEmailRequest();
                sendEmailRequest.withSource(payLoad.getFrom());
                sendEmailRequest.setMessage(new Message().withSubject(new Content().withData(payLoad.getSubject())
                        .withCharset(WITHCHARSET)).withBody(new Body().withText(new Content()
                        .withData(getTemplatedMessage(templateData, payLoad.getBodyText())).withCharset(WITHCHARSET))
                        .withHtml(new Content()
                                .withData(getTemplatedMessage(templateData, payLoad.getBodyHtml())).withCharset(WITHCHARSET))));
                sendEmailRequest.setDestination(new Destination().withToAddresses(payLoad.getTo().get(i).getEmail()));
                logger.info("{}", sendEmailRequest);
                try {
                    logger.info("Attempting to send bulk emails through Amazon SES by using the AWS SDK for Java....");
                    SendEmailResult sendEmailResult = client1.getAmazonSimpleEmailService().sendEmail(sendEmailRequest);
                    logger.info("{}", sendEmailResult.getMessageId());
                    logger.info("Email sent.....");
                } catch (Exception ex) {
                    logger.error("The email was not sent..!", ex);
                }
            } else {
                logger.debug("Wrong email format. Email is ignored -", payLoad.getTo().get(i).getRawEmail());
            }
        }
        logger.info("Out of for loop !");
    }


    public String getTemplatedMessage(Map<String, String> model, String bodyMessage) {
        String message = "";
        try {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);

            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("myTemplate", bodyMessage);
            configuration.setTemplateLoader(stringTemplateLoader);
            configuration.setDefaultEncoding(WITHCHARSET);
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);


            Template template = configuration.getTemplate("myTemplate");
            StringWriter stringWriter = new StringWriter();
            template.process(model, stringWriter);
            message = stringWriter.toString();
        } catch (IOException | TemplateException e) {
            logger.error("{}", e);
        }
        logger.info(message);
        return message;
    }

}