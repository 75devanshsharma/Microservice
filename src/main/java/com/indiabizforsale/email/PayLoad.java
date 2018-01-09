package com.indiabizforsale.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;


public class PayLoad {

    private ArrayList<Recipient> to;
    private String from;
    private String templateId;
    private String fromName;
    private String bodyHtml;
    private String bodyText;
    private String subject;


    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }




    /**
     * <h1>To get template data for a single recipient</h1>
     *
     * @return JSon string
     * @throws JsonProcessingException
     */
    public String getFirstTemplate() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.getTo().get(0).getTemplateData());

    }

    public String getFirstEmail() {
        return this.getTo().get(0).getEmail();
    }


    public int getCount() {
        int count;
        count = this.getTo().size();
        return count;
    }

    public ArrayList<Recipient> getTo() {
        return to;
    }

    public void setTo(ArrayList<Recipient> to) {
        this.to = to;
    }

    /**
     * <h1>To return the email in the form " name of sender <email> " .</h1>
     *
     * @return email
     */
    public String getFrom() {
        return fromName + "<" + from + ">";
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getRawFrom() {
        return from;
    }

}
