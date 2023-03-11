package com.simplifysynergy.repository;

import com.simplifysynergy.domain.Receipt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Receipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceiptRepository extends ReactiveMongoRepository<Receipt, String> {
    Flux<Receipt> findAllBy(Pageable pageable);
}
