package it.justin.service;

import it.justin.apiwrapper.ApiWrapperService;
import it.justin.apiwrapper.ApiWrapperServiceImpl;
import it.justin.apiwrapper.ApiWrapperServiceProperties;
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
        return new ApiWrapperServiceImpl(apiWrapperServiceProperties());
    }

    @Bean
    ApiWrapperServiceProperties apiWrapperServiceProperties(){
        return new ApiWrapperServiceProperties();
    }
}
