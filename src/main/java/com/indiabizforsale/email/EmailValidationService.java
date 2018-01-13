package com.indiabizforsale.email;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailValidationService {
    private static final Logger logger = LoggerFactory.getLogger(EmailValidationService.class);

    //    /**
//     * <h1>For validating the email format by passing the email as a parameter. </h1>
//     *
//     * @param email
//     * @return flag of type boolean
//     */
    public boolean isValid(String email) {
        boolean flag = false;
        EmailValidator ev = EmailValidator.getInstance();
        if (ev.isValid(email)) {
            logger.info("Valid Email");
            flag = true;
        } else {
            logger.info("Invalid Email");
        }
        return flag;
    }
}
