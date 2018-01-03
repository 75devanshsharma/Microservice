package com.indiabizforsale.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class PayLoad {

    private ArrayList<Recipient> to;
    private String from;
    private String templateId;
    private String fromName;

    //To get template data for a single recipient
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

    //To return the email in the form " name of sender <email> " .
    public String getFrom() {
        from = fromName + "<" + from + ">";
        return from;
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

}
