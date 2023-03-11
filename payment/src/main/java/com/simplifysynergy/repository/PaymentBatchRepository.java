package com.simplifysynergy.repository;

import com.simplifysynergy.domain.PaymentBatch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the PaymentBatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentBatchRepository extends ReactiveMongoRepository<PaymentBatch, String> {
    Flux<PaymentBatch> findAllBy(Pageable pageable);
}
