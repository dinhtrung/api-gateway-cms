package com.ft.repository;

import com.ft.domain.ContentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ContentType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentTypeRepository extends ReactiveMongoRepository<ContentType, String> {


    Flux<ContentType> findAllBy(Pageable pageable);

}
