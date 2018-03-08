package com.indiabizforsale.email.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Recipient {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Recipient.class);
    private String email;
    private Map<String, Object> templateData;

    public Map<String, Object> getTemplateData() {
        return templateData;
    }

    public void setTemplateData(Map<String, Object> templateData) {
        this.templateData = templateData;
    }

    /**
     * <h1>To convert the template data of Map into JSon.</h1>
     *
     * @return JSon
     * @throws JsonProcessingException
     */
    public String getTemplateDataJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        return objectMapper.writeValueAsString(templateData);
    }

    /**
     * <p>
     * This method checks if the name of the recipient is present or not in the template data.
     * If present then the email is sent along with the name in th form name <email> otherwise
     * the email is returned.
     * </p>
     *
     * @return email along with name ,if available.
     */
    public String getEmail() {
        if (getTemplateData().containsKey("name")) {
            String mail = getTemplateValue("name") + " <" + getRawEmail() + ">";
            logger.info("Email format : {}", mail);
            return mail;
        } else
            return getRawEmail();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRawEmail() {
        return email;
    }

    private Object getTemplateValue(String key) {
        return templateData.get(key);
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "email='" + email + '\'' +
                ", templateData=" + templateData +
                '}';
    }
}