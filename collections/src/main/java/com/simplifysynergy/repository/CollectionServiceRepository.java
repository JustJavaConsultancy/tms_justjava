package com.simplifysynergy.repository;

import com.simplifysynergy.domain.CollectionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the CollectionService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollectionServiceRepository extends ReactiveMongoRepository<CollectionService, String> {
    Flux<CollectionService> findAllBy(Pageable pageable);
}
