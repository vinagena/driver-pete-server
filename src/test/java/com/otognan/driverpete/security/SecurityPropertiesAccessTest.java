package com.otognan.driverpete.security;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.apache.log4j.Logger;


@Configuration
//@PropertySource("classpath:social.properties")
class TestConfiguration {}


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=TestConfiguration.class)
public class SecurityPropertiesAccessTest {
    
    @Autowired
    private ApplicationContext appContext;
    
    static Logger log = Logger.getLogger("TEST_LOGGER");

    @Test
    public void testSocialPropertyAccess() throws Exception {
        String property = appContext.getEnvironment().getProperty("mycustomprop.x");
        log.error("-------------------------------------");
        log.error(property);
        assertThat(property, equalTo("xxxxxxx"));
    }
    
}
