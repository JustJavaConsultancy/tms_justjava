package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.ApprovalLineDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.ApprovalLine}.
 */
public interface ApprovalLineService {
    /**
     * Save a approvalLine.
     *
     * @param approvalLineDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ApprovalLineDTO> save(ApprovalLineDTO approvalLineDTO);

    /**
     * Updates a approvalLine.
     *
     * @param approvalLineDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ApprovalLineDTO> update(ApprovalLineDTO approvalLineDTO);

    /**
     * Partially updates a approvalLine.
     *
     * @param approvalLineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ApprovalLineDTO> partialUpdate(ApprovalLineDTO approvalLineDTO);

    /**
     * Get all the approvalLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ApprovalLineDTO> findAll(Pageable pageable);

    /**
     * Returns the number of approvalLines available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" approvalLine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ApprovalLineDTO> findOne(String id);

    /**
     * Delete the "id" approvalLine.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
