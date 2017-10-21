package it.justin.service;

import it.justin.apiwrapper.ApiWrapperService;
import it.justin.apiwrapper.ApiWrapperServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

    @Bean
    ApiWrapperService apiWrapperRequest() {
        return new ApiWrapperServiceImpl();
    }
}
