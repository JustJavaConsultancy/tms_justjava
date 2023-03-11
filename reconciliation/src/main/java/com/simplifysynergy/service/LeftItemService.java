package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.LeftItemDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.LeftItem}.
 */
public interface LeftItemService {
    /**
     * Save a leftItem.
     *
     * @param leftItemDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<LeftItemDTO> save(LeftItemDTO leftItemDTO);

    /**
     * Updates a leftItem.
     *
     * @param leftItemDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<LeftItemDTO> update(LeftItemDTO leftItemDTO);

    /**
     * Partially updates a leftItem.
     *
     * @param leftItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<LeftItemDTO> partialUpdate(LeftItemDTO leftItemDTO);

    /**
     * Get all the leftItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<LeftItemDTO> findAll(Pageable pageable);

    /**
     * Returns the number of leftItems available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" leftItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<LeftItemDTO> findOne(String id);

    /**
     * Delete the "id" leftItem.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
