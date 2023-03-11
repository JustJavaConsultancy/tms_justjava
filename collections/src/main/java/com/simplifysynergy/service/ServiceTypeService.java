package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.ServiceTypeDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.ServiceType}.
 */
public interface ServiceTypeService {
    /**
     * Save a serviceType.
     *
     * @param serviceTypeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ServiceTypeDTO> save(ServiceTypeDTO serviceTypeDTO);

    /**
     * Updates a serviceType.
     *
     * @param serviceTypeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ServiceTypeDTO> update(ServiceTypeDTO serviceTypeDTO);

    /**
     * Partially updates a serviceType.
     *
     * @param serviceTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ServiceTypeDTO> partialUpdate(ServiceTypeDTO serviceTypeDTO);

    /**
     * Get all the serviceTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ServiceTypeDTO> findAll(Pageable pageable);

    /**
     * Returns the number of serviceTypes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" serviceType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ServiceTypeDTO> findOne(String id);

    /**
     * Delete the "id" serviceType.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
