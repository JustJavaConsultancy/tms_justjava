package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.RequestTypeDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.RequestType}.
 */
public interface RequestTypeService {
    /**
     * Save a requestType.
     *
     * @param requestTypeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RequestTypeDTO> save(RequestTypeDTO requestTypeDTO);

    /**
     * Updates a requestType.
     *
     * @param requestTypeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RequestTypeDTO> update(RequestTypeDTO requestTypeDTO);

    /**
     * Partially updates a requestType.
     *
     * @param requestTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RequestTypeDTO> partialUpdate(RequestTypeDTO requestTypeDTO);

    /**
     * Get all the requestTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<RequestTypeDTO> findAll(Pageable pageable);

    /**
     * Returns the number of requestTypes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" requestType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RequestTypeDTO> findOne(String id);

    /**
     * Delete the "id" requestType.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
