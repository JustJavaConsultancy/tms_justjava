package com.simplifysynergy.repository;

import com.simplifysynergy.domain.ChartOfAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ChartOfAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChartOfAccountRepository extends ReactiveMongoRepository<ChartOfAccount, String> {
    Flux<ChartOfAccount> findAllBy(Pageable pageable);
}
