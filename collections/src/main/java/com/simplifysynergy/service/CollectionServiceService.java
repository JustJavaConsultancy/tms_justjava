package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.CollectionServiceDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.CollectionService}.
 */
public interface CollectionServiceService {
    /**
     * Save a collectionService.
     *
     * @param collectionServiceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CollectionServiceDTO> save(CollectionServiceDTO collectionServiceDTO);

    /**
     * Updates a collectionService.
     *
     * @param collectionServiceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CollectionServiceDTO> update(CollectionServiceDTO collectionServiceDTO);

    /**
     * Partially updates a collectionService.
     *
     * @param collectionServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CollectionServiceDTO> partialUpdate(CollectionServiceDTO collectionServiceDTO);

    /**
     * Get all the collectionServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CollectionServiceDTO> findAll(Pageable pageable);

    /**
     * Returns the number of collectionServices available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" collectionService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CollectionServiceDTO> findOne(String id);

    /**
     * Delete the "id" collectionService.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
