package com.indiabizforsale.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import java.io.IOException;


public class PayLoadTest {
    @Test
    public void payloadTest()
     {
        final org.slf4j.Logger logger = LoggerFactory.getLogger(PayLoadTest.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"fromName\":\"mkyong\",\"from\":\"raj@indiabizforsale.com\",\"to\":[{\"email\":\"devansh@indiabizforsale.com\"},{\"email\":\"75devanshsharma@gmail.com\",\"templateData\":{\"name\":\"Rahul\"}}],\"templateId\":\"myTemplate\"}";
         PayLoad payLoad1 = null;
         try {
             payLoad1 = mapper.readValue(json, PayLoad.class);
         } catch (IOException e) {
             e.printStackTrace();
         }
         String s = payLoad1.getTo().toString();
        logger.info(s);
    }
}
