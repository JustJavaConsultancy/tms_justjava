package com.simplifysynergy.repository;

import com.simplifysynergy.domain.ApprovalRoute;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the ApprovalRoute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalRouteRepository extends ReactiveMongoRepository<ApprovalRoute, String> {
    Flux<ApprovalRoute> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<ApprovalRoute> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<ApprovalRoute> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<ApprovalRoute> findOneWithEagerRelationships(String id);
}
