package com.simplifysynergy.repository;

import com.simplifysynergy.domain.ServiceType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ServiceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceTypeRepository extends ReactiveMongoRepository<ServiceType, String> {
    Flux<ServiceType> findAllBy(Pageable pageable);
}
