package com.rank.reactive.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ReactiveClientApp {
    public static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) {
        SpringApplication.run(ReactiveClientApp.class, args);
    }


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder().baseUrl(BASE_URL).build();

    }


}
