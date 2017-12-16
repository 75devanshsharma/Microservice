package com.indiabizforsale.email;

import java.util.Map;

public class MultipleRecipient {

        private String email;
        private Map<String,String> templateData;

        public String getEmail () {
        return email;
        }

        public void setEmail (String email){
        this.email = email;
        }

}