package com.norbert.translator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class TranslatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(TranslatorApplication.class,args);
    }
}