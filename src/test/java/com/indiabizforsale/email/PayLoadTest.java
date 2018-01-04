package com.indiabizforsale.email;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class PayLoadTest {
    @Test
    public void payloadTest()
     {
        final org.slf4j.Logger logger = LoggerFactory.getLogger(PayLoadTest.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"fromName\":\"mkyong\",\"from\":\"raj@indiabizforsale.com\",\"to\":[{\"email\":\"75devanshsharma@gmail.com\",\"templateData\":{\"name\":\"Rahul\"}}],\"templateId\":\"myTemplate\"}";
         PayLoad payLoad1 =  new PayLoad();
         Map<String,String>  map = new HashMap<>();
         map.put("name","Dev");
         ArrayList<Recipient> to = new ArrayList<>();
         Recipient recipient = new Recipient();
         recipient.setEmail("devansh@indiabizforsale.com");
         recipient.setTemplateData(map);
         payLoad1.setFromName("Raj");
         payLoad1.setFrom("raj@indiabizforsale.com");
         payLoad1.setTemplateId("MyTemplate");
         to.add(0,recipient);
         payLoad1.setTo(to);
         try {
             PayLoad payLoad = mapper.readValue(json, PayLoad.class);
             assertEquals("Outcome",payLoad,payLoad1);
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
}
