package com.ft.repository;

import com.ft.domain.Domain;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Domain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomainRepository extends ReactiveMongoRepository<Domain, String> {


    Flux<Domain> findAllBy(Pageable pageable);

}
