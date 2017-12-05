package com.example.webflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Created by Can.Mogol on 12/4/2017.
 */
@Slf4j
public class WebfluxTest {

    public static final String HOST_PORT_URL = "http://localhost:8080/";
    public static final String PERSON_API_URL = HOST_PORT_URL + "persons/";
    public static final String GET_PERSON_API_URL = PERSON_API_URL + "%1s";

    @Test
    public void monoTest() throws Exception {
        WebClient webClient = WebClient.create();

//        Mono<PersonEntity> person = webClient.get()
//            .uri(String.format(GET_PERSON_API_URL, "i1"))
//            .accept(MediaType.APPLICATION_JSON)
//            .exchange()
//            .then(response -> response.bodyToMono(Person.class));
//
    }

}
