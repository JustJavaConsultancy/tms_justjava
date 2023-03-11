package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.RightItemDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.RightItem}.
 */
public interface RightItemService {
    /**
     * Save a rightItem.
     *
     * @param rightItemDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RightItemDTO> save(RightItemDTO rightItemDTO);

    /**
     * Updates a rightItem.
     *
     * @param rightItemDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RightItemDTO> update(RightItemDTO rightItemDTO);

    /**
     * Partially updates a rightItem.
     *
     * @param rightItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RightItemDTO> partialUpdate(RightItemDTO rightItemDTO);

    /**
     * Get all the rightItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<RightItemDTO> findAll(Pageable pageable);

    /**
     * Returns the number of rightItems available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" rightItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RightItemDTO> findOne(String id);

    /**
     * Delete the "id" rightItem.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
