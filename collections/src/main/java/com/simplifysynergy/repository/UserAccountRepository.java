package com.simplifysynergy.repository;

import com.simplifysynergy.domain.UserAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the UserAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAccountRepository extends ReactiveMongoRepository<UserAccount, String> {
    Flux<UserAccount> findAllBy(Pageable pageable);
}
