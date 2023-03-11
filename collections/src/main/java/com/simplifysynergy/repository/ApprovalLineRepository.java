package com.simplifysynergy.repository;

import com.simplifysynergy.domain.ApprovalLine;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ApprovalLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalLineRepository extends ReactiveMongoRepository<ApprovalLine, String> {
    Flux<ApprovalLine> findAllBy(Pageable pageable);
}
