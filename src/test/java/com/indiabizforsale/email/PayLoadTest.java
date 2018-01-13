package com.indiabizforsale.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class PayLoadTest {
    @Test
    public void payloadTest() throws IOException {
        final org.slf4j.Logger logger = LoggerFactory.getLogger(PayLoadTest.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"to\":[{\"email\":\"devansh@indiabizforsale.com\",\"templateData\":{\"name\":\"Dev\"}}],\"from\":\"raj@indiabizforsale.com\",\"fromName\":\"Raj\",\"templateId\":\"MyTemplate1\"}";
        logger.info(json);
        PayLoad payLoad = mapper.readValue(json, PayLoad.class);
        assertEquals("Passed", "Raj", payLoad.getFromName());
        assertEquals("devansh@indiabizforsale.com", payLoad.getTo().get(0).getRawEmail());
        assertEquals("raj@indiabizforsale.com", payLoad.getRawFrom());
        assertEquals("MyTemplate1", payLoad.getTemplateId());
    }
}
