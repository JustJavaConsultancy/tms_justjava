package com.simplifysynergy.repository;

import com.simplifysynergy.domain.ReconciliationItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ReconciliationItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReconciliationItemRepository extends ReactiveMongoRepository<ReconciliationItem, String> {
    Flux<ReconciliationItem> findAllBy(Pageable pageable);
}
