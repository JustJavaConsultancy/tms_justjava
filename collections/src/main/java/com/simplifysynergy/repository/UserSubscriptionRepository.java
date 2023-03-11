package com.simplifysynergy.repository;

import com.simplifysynergy.domain.UserSubscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the UserSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSubscriptionRepository extends ReactiveMongoRepository<UserSubscription, String> {
    Flux<UserSubscription> findAllBy(Pageable pageable);
}
