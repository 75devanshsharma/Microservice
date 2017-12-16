package com.indiabizforsale.email;

import java.util.ArrayList;
import java.util.Arrays;

public class PayLoad {

    private ArrayList<MultipleRecipient> to;
    private String from;
    private String templateId;
    private String fromName;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTemplateID() {
        return templateId;
    }

    public void setTemplateID(String templateID) {
        this.templateId = templateID;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

}
