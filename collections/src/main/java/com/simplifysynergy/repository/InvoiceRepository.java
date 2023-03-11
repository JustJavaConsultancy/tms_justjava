package com.simplifysynergy.repository;

import com.simplifysynergy.domain.Invoice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends ReactiveMongoRepository<Invoice, String> {
    Flux<Invoice> findAllBy(Pageable pageable);
}
