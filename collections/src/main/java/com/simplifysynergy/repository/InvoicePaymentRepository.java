package com.simplifysynergy.repository;

import com.simplifysynergy.domain.InvoicePayment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the InvoicePayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoicePaymentRepository extends ReactiveMongoRepository<InvoicePayment, String> {
    Flux<InvoicePayment> findAllBy(Pageable pageable);
}
