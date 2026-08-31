package com.acciojobs.bms_august.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class SystemConfiguration {

    /**
     * Our api will use this email address to sending out the notification.
     */
    public String apiEmailAddress = "6206472735rahul@gmail.com";


    public String apiEmailPassword = "ceffelhhjfderwei";


    @Bean
    public JavaMailSender generateJavaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.starttls.enable", true);
        javaMailSender.setJavaMailProperties(mailProperties);
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername(apiEmailAddress);
        javaMailSender.setPassword(apiEmailPassword);
        return javaMailSender;
    }

    @Bean
    public ExecutorService generateExecutorService(){
        return Executors.newFixedThreadPool(5);
    }

}
