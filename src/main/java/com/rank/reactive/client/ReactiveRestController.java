package com.rank.reactive.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequiredArgsConstructor
public class ReactiveRestController {


    private final WebClient webClient;
    //endregion

    private final ProviderService providerService;

    @GetMapping("/provider/{id1}/{id2}/{id3}")
    public String getPerson(@PathVariable int id1, @PathVariable int id2, @PathVariable int id3) {
        providerService.getProviderWithRestTemplate(id1, id2, id3);
        return "Processing with RestTemplate...";
    }

    @GetMapping("/provider-reactive/{id1}/{id2}/{id3}")
    public String getProviderReactive(@PathVariable int id1, @PathVariable int id2, @PathVariable int id3) {
        providerService.getProviderWithReactive(id1, id2, id3);
        return "Processing with WebClient...";
    }

    @PostMapping("/provider")
    public Provider addProvider() {

        Provider provider = Provider.builder()
                .id(7)
                .name("Netent")
                .build();

        return webClient.post()
                .uri("/provider")
                .body(Mono.just(provider), Provider.class)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Provider.class))
                .block();
    }

    @GetMapping("/providers")
    public String getProviders() {
        webClient.get()
                .uri("/providers")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Provider.class)
                .subscribe(provider -> {
                    log.info("providers::::::-----");
                    log.info(provider);
                });

        return "Done";
    }

    @GetMapping(path = "/providers-client-stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<Provider> getProvidersAsEventStream() {
        return webClient.get()
                .uri("/providers-server-stream")
                .retrieve()
                .bodyToFlux(Provider.class);
    }
}
