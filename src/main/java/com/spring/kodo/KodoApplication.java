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

        Properties properties = new Properties();

        properties.setProperty("spring.datasource.url", System.getenv("DATASOURCE_URL"));
        properties.setProperty("spring.datasource.username", System.getenv("DATASOURCE_USERNAME"));
        properties.setProperty("spring.datasource.password", System.getenv("DATASOURCE_PASSWORD"));

        application.setDefaultProperties(properties);
        application.run(args);
    }

}
