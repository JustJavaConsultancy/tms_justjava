package com.simplifysynergy.repository;

import com.simplifysynergy.domain.Payer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Payer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayerRepository extends ReactiveMongoRepository<Payer, String> {
    Flux<Payer> findAllBy(Pageable pageable);
}
