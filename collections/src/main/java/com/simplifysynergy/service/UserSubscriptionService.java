package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.UserSubscriptionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.UserSubscription}.
 */
public interface UserSubscriptionService {
    /**
     * Save a userSubscription.
     *
     * @param userSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserSubscriptionDTO> save(UserSubscriptionDTO userSubscriptionDTO);

    /**
     * Updates a userSubscription.
     *
     * @param userSubscriptionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserSubscriptionDTO> update(UserSubscriptionDTO userSubscriptionDTO);

    /**
     * Partially updates a userSubscription.
     *
     * @param userSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserSubscriptionDTO> partialUpdate(UserSubscriptionDTO userSubscriptionDTO);

    /**
     * Get all the userSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserSubscriptionDTO> findAll(Pageable pageable);

    /**
     * Returns the number of userSubscriptions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" userSubscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserSubscriptionDTO> findOne(String id);

    /**
     * Delete the "id" userSubscription.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
