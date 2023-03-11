package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.RequestContentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.RequestContent}.
 */
public interface RequestContentService {
    /**
     * Save a requestContent.
     *
     * @param requestContentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RequestContentDTO> save(RequestContentDTO requestContentDTO);

    /**
     * Updates a requestContent.
     *
     * @param requestContentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RequestContentDTO> update(RequestContentDTO requestContentDTO);

    /**
     * Partially updates a requestContent.
     *
     * @param requestContentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RequestContentDTO> partialUpdate(RequestContentDTO requestContentDTO);

    /**
     * Get all the requestContents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<RequestContentDTO> findAll(Pageable pageable);

    /**
     * Returns the number of requestContents available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" requestContent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RequestContentDTO> findOne(String id);

    /**
     * Delete the "id" requestContent.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
