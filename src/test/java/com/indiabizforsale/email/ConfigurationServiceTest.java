package com.indiabizforsale.email;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigurationServiceTest {

    private ConfigurationService configurationService = new ConfigurationService();

    @Test
    public void getCredentials() {
        configurationService.setEmailCredentials();
        assertNotNull(System.getProperty(ConfigurationService.AWS_ACCESS_KEY));
        assertNotNull(System.getProperty(ConfigurationService.AWS_SECRET_KEY));
    }

}