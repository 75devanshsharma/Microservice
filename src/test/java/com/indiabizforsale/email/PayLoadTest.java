package com.indiabizforsale.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class PayLoadTest {
    @Test
    public void payloadTest()
     {
        final org.slf4j.Logger logger = LoggerFactory.getLogger(PayLoadTest.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"to\":[{\"email\":\"devansh@indiabizforsale.com\",\"templateData\":{\"name\":\"Dev\"}}],\"from\":\"raj@indiabizforsale.com\",\"fromName\":\"Raj\",\"templateId\":\"MyTemplate1\"}";
        logger.info(json);
//         Map<String,String>  map = new HashMap<>();
//         map.put("name","Dev");
//         ArrayList<Recipient> to = new ArrayList<>();
//         Recipient recipient = new Recipient();
//         recipient.setEmail("devansh@indiabizforsale.com");
//         recipient.setTemplateData(map);
//         payLoad1.setFromName("Raj");
//         payLoad1.setFrom("raj@indiabizforsale.com");
//         payLoad1.setTemplateId("MyTemplate");
//         to.add(0,recipient);
//         payLoad1.setTo(to);
         try {
             PayLoad payLoad = mapper.readValue(json, PayLoad.class);
             assertEquals("Passed", "Raj", payLoad.getFromName());
             assertEquals("devansh@indiabizforsale.com",payLoad.getTo().get(0).getRawEmail());
             assertEquals("raj@indiabizforsale.com",payLoad.getRawFrom());
             assertEquals("MyTemplate1",payLoad.getTemplateId());
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
}
