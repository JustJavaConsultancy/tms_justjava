package com.simplifysynergy.repository;

import com.simplifysynergy.domain.Subscriber;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Subscriber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriberRepository extends ReactiveMongoRepository<Subscriber, String> {
    Flux<Subscriber> findAllBy(Pageable pageable);
}
