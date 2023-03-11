package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.SubscriberDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.Subscriber}.
 */
public interface SubscriberService {
    /**
     * Save a subscriber.
     *
     * @param subscriberDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SubscriberDTO> save(SubscriberDTO subscriberDTO);

    /**
     * Updates a subscriber.
     *
     * @param subscriberDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SubscriberDTO> update(SubscriberDTO subscriberDTO);

    /**
     * Partially updates a subscriber.
     *
     * @param subscriberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SubscriberDTO> partialUpdate(SubscriberDTO subscriberDTO);

    /**
     * Get all the subscribers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SubscriberDTO> findAll(Pageable pageable);

    /**
     * Returns the number of subscribers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" subscriber.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SubscriberDTO> findOne(String id);

    /**
     * Delete the "id" subscriber.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
