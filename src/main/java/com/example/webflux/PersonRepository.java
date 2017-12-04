package com.example.webflux;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Created by Can.Mogol on 12/4/2017.
 */
public interface PersonRepository extends ReactiveMongoRepository<PersonEntity, String> {

}
