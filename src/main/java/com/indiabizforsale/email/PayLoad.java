package com.indiabizforsale.email;

import com.amazonaws.services.simpleemail.model.BulkEmailDestination;
import com.amazonaws.services.simpleemail.model.Destination;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class PayLoad {

    private ArrayList<Recipient> to;
    private String from;
    private String templateId;
    private String fromName;
    private String msg;


    public String getFirstTemplate() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.getTo().get(0).getTemplateData());

    }

//    public String getFirstEmail() throws JsonProcessingException {
//        String email = this.getTo().get(0).getEmail();
////        email="Raj geriya <"+email+">";
//        String name = this.getName();
//        email = name+"<"+email+">";
//        return email;
//    }

    public String getFirstEmail() {
        return this.getTo().get(0).getEmail();
    }


    public int getCount()
    {
        int count;
        count = this.getTo().size();
        return  count;
    }

//    public String getAllEmail()
//    {
//        int i;
//        for(i=0; i<20; i++)
//        {
//            return this.getTo().get(i).getEmail();
//        }
//        return null;
//    }

//    public String[] getAllTemplateData() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String[] s= new String[20];
//        int i;
//        for(i=0;i<20;i++)
//        {
//            s[i]= objectMapper.writeValueAsString(this.getTo().get(i).getTemplateData());
//            return s;
//        }
//        return s;
//    }

    public ArrayList<Recipient> getTo() {
        return to;
    }

    public void setTo(ArrayList<Recipient> to) {
        this.to = to;
    }


    public String getFrom() {
        from = fromName + "<" + from +">";
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
