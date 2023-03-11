package com.simplifysynergy.repository;

import com.simplifysynergy.domain.RightItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the RightItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RightItemRepository extends ReactiveMongoRepository<RightItem, String> {
    Flux<RightItem> findAllBy(Pageable pageable);
}
