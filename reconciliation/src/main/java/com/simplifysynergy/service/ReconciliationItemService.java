package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.ReconciliationItemDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.ReconciliationItem}.
 */
public interface ReconciliationItemService {
    /**
     * Save a reconciliationItem.
     *
     * @param reconciliationItemDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ReconciliationItemDTO> save(ReconciliationItemDTO reconciliationItemDTO);

    /**
     * Updates a reconciliationItem.
     *
     * @param reconciliationItemDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ReconciliationItemDTO> update(ReconciliationItemDTO reconciliationItemDTO);

    /**
     * Partially updates a reconciliationItem.
     *
     * @param reconciliationItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ReconciliationItemDTO> partialUpdate(ReconciliationItemDTO reconciliationItemDTO);

    /**
     * Get all the reconciliationItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ReconciliationItemDTO> findAll(Pageable pageable);

    /**
     * Returns the number of reconciliationItems available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" reconciliationItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ReconciliationItemDTO> findOne(String id);

    /**
     * Delete the "id" reconciliationItem.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
