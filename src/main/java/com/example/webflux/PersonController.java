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
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

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
            "\tvar div = document.createElement(\"div\");\n" +
            "\tvar person = JSON.parse(e.data);\n" +
            "\tvar index = Number(person.id);\n" +
            "\tvar row = document.getElementById('tr'+index);\n" +
            "\tif(row === null){\n" +
            "\t\tvar tr = document.createElement('tr');\n" +
            "\t\ttr.id = 'tr'+index;\n" +
            "\t\tvar td = document.createElement('td');\t\n" +
            "\t\ttd.id = 'td'+index;\n" +
            "\t\ttd.innerText = e.data;\n" +
            "\t\ttr.appendChild(td);\n" +
            "\t\tvar table = document.getElementById('personTable');\n" +
            "\t\ttable.appendChild(tr);\n" +
            "\t}else{\n" +
            "\t\tvar td = document.getElementById('td'+index);\n" +
            "\t\ttd.innerText = e.data;\n" +
            "\t}\n" +
            "}" +
            "</script>" +
            "<table id='personTable'>" +
            "</table>" +
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
    Flux<PersonEntity> streamGenericStream() {
        final Random random = new Random();
        return Flux
            .interval(Duration.ofMillis(1000))
            .map(tick -> {
                final long nextLong = random.nextLong();
                Long id = Math.abs(nextLong % 5);
                return new PersonEntity(String.valueOf(id), String.valueOf(nextLong), new Date());
            });
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
