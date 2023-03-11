package com.simplifysynergy.repository;

import com.simplifysynergy.domain.PaymentInstruction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the PaymentInstruction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentInstructionRepository extends ReactiveMongoRepository<PaymentInstruction, String> {
    Flux<PaymentInstruction> findAllBy(Pageable pageable);
}
