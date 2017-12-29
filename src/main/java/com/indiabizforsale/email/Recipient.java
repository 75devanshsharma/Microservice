package com.indiabizforsale.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Recipient {

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
                return objectMapper.writeValueAsString(templateData);
        }

        public String getEmail () {
        return email;
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