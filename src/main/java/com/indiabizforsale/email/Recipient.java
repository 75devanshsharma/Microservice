package com.indiabizforsale.email;

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