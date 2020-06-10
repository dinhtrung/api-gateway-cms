package com.ft.repository;

import com.ft.domain.ContentField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ContentField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentFieldRepository extends ReactiveMongoRepository<ContentField, String> {


    Flux<ContentField> findAllBy(Pageable pageable);

}
