package com.simplifysynergy.repository;

import com.simplifysynergy.domain.RequestContent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the RequestContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestContentRepository extends ReactiveMongoRepository<RequestContent, String> {
    Flux<RequestContent> findAllBy(Pageable pageable);
}
