package com.simplifysynergy.repository;

import com.simplifysynergy.domain.InstitutionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the InstitutionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstitutionTypeRepository extends ReactiveMongoRepository<InstitutionType, String> {
    Flux<InstitutionType> findAllBy(Pageable pageable);
}
