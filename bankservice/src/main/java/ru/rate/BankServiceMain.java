package ru.rate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankServiceMain {
    public static void main(String[] args) {
        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "connection,content-length,host");
        SpringApplication.run(BankServiceMain.class, args);
    }
}
