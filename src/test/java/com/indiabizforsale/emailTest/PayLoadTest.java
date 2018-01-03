package com.indiabizforsale.emailTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indiabizforsale.email.PayLoad;

import java.io.IOException;


public class PayLoadTest {
    public static void main(String[] args) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"fromName\":\"mkyong\",\"from\":\"raj@indiabizforsale.com\",\"to\":[{\"email\":\"devansh@indiabizforsale.com\"},{\"email\":\"75devanshsharma@gmail.com\",\"templateData\":{\"name\":\"Rahul\"}}],\"templateId\":\"myTemplate\"}";
        PayLoad payLoad1 = mapper.readValue(json, PayLoad.class);
        String s = payLoad1.getTo().toString();
        System.out.println(s);

    }
}
