package com.simplifysynergy.repository;

import com.simplifysynergy.domain.RequestType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the RequestType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestTypeRepository extends ReactiveMongoRepository<RequestType, String> {
    Flux<RequestType> findAllBy(Pageable pageable);
}
