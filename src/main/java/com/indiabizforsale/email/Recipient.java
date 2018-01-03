package com.indiabizforsale.email;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;


import java.util.Map;

public class Recipient {
        private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmailSender.class);
        private String email;
        private Map<String,String> templateData;



        public Map<String, String> getTemplateData() {
                return templateData;
        }

        public void setTemplateData(Map<String, String> templateData) {
                this.templateData = templateData;
        }

        public String getTemplateDataJson() throws JsonProcessingException {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

                return objectMapper.writeValueAsString(templateData);
        }

        public String getEmail ()
        {
                if(getTemplateData().containsKey("name"))
                {
                        String mail = getTemplateValue("name") + " <" + getRawEmail() + ">";
                        logger.info(mail);
                        return mail;
                }
                        else
                        return getRawEmail();
        }

        public String getRawEmail()
        {
                return email;
        }

        public String getTemplateValue(String key)
        {
                return templateData.get(key);
        }

        public void setEmail (String email){
        this.email = email;
        }

        public boolean checkKey()
        {
                return templateData.containsKey("name");
        }
        @Override
        public String toString() {
                return "Recipient{" +
                        "email='" + email + '\'' +
                        ", templateData=" + templateData +
                        '}';
        }
}