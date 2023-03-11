package com.simplifysynergy.repository;

import com.simplifysynergy.domain.LeftItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the LeftItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeftItemRepository extends ReactiveMongoRepository<LeftItem, String> {
    Flux<LeftItem> findAllBy(Pageable pageable);
}
