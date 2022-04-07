package com.rank.reactive.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.rank.reactive.client.ReactiveClientApp.BASE_URL;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProviderService {

    public static final String PROVIDER_ID = "/provider/{id}";
    private final WebClient webClient;
    private final RestTemplate restTemplate;

    public void getProviderWithRestTemplate(int id1, int id2, int id3) {
        log.info("Template.............");
        var uri = BASE_URL.concat(PROVIDER_ID);

        //First Call
        var result = restTemplate.getForObject(uri, Provider.class, Map.of("id", id1));
        log.info(result);

        //Second Call
        var result2 = restTemplate.getForObject(uri, Provider.class, Map.of("id", id2));
        log.info(result2);

        //Third Call
        var result3 = restTemplate.getForObject(uri, Provider.class, Map.of("id", id3));
        log.info(result3);
    }

    public void getProviderWithReactive(int id1, int id2, int id3) {
        log.info("Reactive.............");

        //First Call
        webClient.get()
                .uri(PROVIDER_ID, id1)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Provider.class)
                .subscribe(log::info);

        //Second Call
        webClient.get()
                .uri(PROVIDER_ID, id2)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Provider.class)
                .subscribe(log::info);

        //Third Call
        webClient.get()
                .uri(PROVIDER_ID, id3)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Provider.class)
                .retry(1)
                .onErrorReturn(new Provider(0,"Fallback Provider")) // fallback on Error
                .subscribe(log::info);

    }
}
