package com.indiabizforsale.email;
import org.slf4j.Logger;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.LoggerFactory;

public class EmailValidationService {
    private static final Logger logger = LoggerFactory.getLogger(EmailValidationService.class);

    public boolean emailValidate(String email)
    {
            boolean flag = false;
            EmailValidator ev = EmailValidator.getInstance();
            if(ev.isValid(email))
            {
                logger.info("Valid Email");
                flag=true;
            }
            else {
                logger.info("Invalid Email");
        }
        return flag;
    }
}
