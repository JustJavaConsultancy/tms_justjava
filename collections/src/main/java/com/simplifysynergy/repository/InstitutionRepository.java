package com.simplifysynergy.repository;

import com.simplifysynergy.domain.Institution;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Institution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstitutionRepository extends ReactiveMongoRepository<Institution, String> {
    Flux<Institution> findAllBy(Pageable pageable);
}
