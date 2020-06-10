package com.ft.repository;

import com.ft.domain.Content;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Content entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentRepository extends ReactiveMongoRepository<Content, String> {


    Flux<Content> findAllBy(Pageable pageable);

}
