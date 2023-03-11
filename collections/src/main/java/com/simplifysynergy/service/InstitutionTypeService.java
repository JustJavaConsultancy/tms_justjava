package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.InstitutionTypeDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.InstitutionType}.
 */
public interface InstitutionTypeService {
    /**
     * Save a institutionType.
     *
     * @param institutionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InstitutionTypeDTO> save(InstitutionTypeDTO institutionTypeDTO);

    /**
     * Updates a institutionType.
     *
     * @param institutionTypeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InstitutionTypeDTO> update(InstitutionTypeDTO institutionTypeDTO);

    /**
     * Partially updates a institutionType.
     *
     * @param institutionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InstitutionTypeDTO> partialUpdate(InstitutionTypeDTO institutionTypeDTO);

    /**
     * Get all the institutionTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InstitutionTypeDTO> findAll(Pageable pageable);

    /**
     * Returns the number of institutionTypes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" institutionType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InstitutionTypeDTO> findOne(String id);

    /**
     * Delete the "id" institutionType.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
