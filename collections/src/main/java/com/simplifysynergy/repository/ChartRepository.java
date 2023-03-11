package com.simplifysynergy.repository;

import com.simplifysynergy.domain.Chart;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Chart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChartRepository extends ReactiveMongoRepository<Chart, String> {
    Flux<Chart> findAllBy(Pageable pageable);
}
