package com.example.webflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.util.HashMap;

/**
 * Created by Can.Mogol on 12/4/2017.
 */
@RestController
public class PersonController {

    @Autowired
    PersonRepository repository;


    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "" +
            "<html>" +
            "<body>" +
            "<script>" +
            "var es = new EventSource(\"/stream/event\");\n" +
            "\n" +
            "es.onmessage = function(e) {\n" +
            "   var div = document.createElement(\"div\");\n" +
            "   div.innerText = e.data;\n" +
            "   document.getElementsByTagName('body')[0].appendChild(div);\n" +
            "}" +
            "</script>" +
            "</body>" +
            "</html>" +
            "";
    }


    @GetMapping("/person")
    public Flux<PersonEntity> getAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/stream/person", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<PersonEntity> streamAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/stream/generic", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<HashMap> streamGeneric() {
        return Flux
            .interval(Duration.ofMillis(1000))
            .map(tick -> new HashMap<String, Long>() {{
                put(String.valueOf(tick), tick);
            }});
    }

    @GetMapping(path = "/stream/event", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<HashMap> streamGenericStream() {
        return Flux
            .interval(Duration.ofMillis(1000))
            .map(tick -> new HashMap<String, Long>() {{
                put(String.valueOf(tick), tick);
            }});
    }

    @GetMapping("/person/{id}")
    public Mono<ResponseEntity<PersonEntity>> getPerson(@PathVariable(value = "id") String id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/person")
    public Mono<PersonEntity> addPerson(@Valid @RequestBody PersonEntity person) {
        return repository.save(person);
    }

    @DeleteMapping("/person/{id}")
    public Mono<ResponseEntity<PersonEntity>> deletePerson(@PathVariable(value = "id") String id) {
        return repository.findById(id)
            .flatMap(person ->
                repository
                    .delete(person)
                    .then(Mono.just(ResponseEntity.ok(person)))
            )
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/person/{id}")
    public Mono<ResponseEntity<PersonEntity>> updatePerson(@PathVariable(value = "id") String id, @Valid @RequestBody PersonEntity person) {
        return repository.findById(id)
            .flatMap(existingPerson -> {
                existingPerson.setName(person.getName());
                return repository.save(existingPerson);
            })
            .map(updatedPerson -> new ResponseEntity<>(updatedPerson, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
