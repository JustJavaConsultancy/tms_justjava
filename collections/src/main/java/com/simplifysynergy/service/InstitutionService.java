package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.InstitutionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.Institution}.
 */
public interface InstitutionService {
    /**
     * Save a institution.
     *
     * @param institutionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InstitutionDTO> save(InstitutionDTO institutionDTO);

    /**
     * Updates a institution.
     *
     * @param institutionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InstitutionDTO> update(InstitutionDTO institutionDTO);

    /**
     * Partially updates a institution.
     *
     * @param institutionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InstitutionDTO> partialUpdate(InstitutionDTO institutionDTO);

    /**
     * Get all the institutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InstitutionDTO> findAll(Pageable pageable);

    /**
     * Returns the number of institutions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" institution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InstitutionDTO> findOne(String id);

    /**
     * Delete the "id" institution.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
