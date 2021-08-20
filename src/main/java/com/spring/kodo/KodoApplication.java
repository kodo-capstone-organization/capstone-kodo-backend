package com.spring.kodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class KodoApplication
{
    public static void main(String[] args)
    {
        SpringApplication application = new SpringApplication(KodoApplication.class);
        application.setDefaultProperties(getCustomApplicationProperties());
        application.run(args);
    }

    private static Properties getCustomApplicationProperties() {
        Properties properties = new Properties();

        // Datasource
        String datasourcePrefix = "spring.datasource.";
        properties.setProperty(datasourcePrefix + "url", System.getenv("DATASOURCE_URL"));
        properties.setProperty(datasourcePrefix + "username", System.getenv("DATASOURCE_USERNAME"));
        properties.setProperty(datasourcePrefix + "password", System.getenv("DATASOURCE_PASSWORD"));

        // Set other custom properties here

        return properties;
    }

}
