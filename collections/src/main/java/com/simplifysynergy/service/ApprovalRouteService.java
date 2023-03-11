package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.ApprovalRouteDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.ApprovalRoute}.
 */
public interface ApprovalRouteService {
    /**
     * Save a approvalRoute.
     *
     * @param approvalRouteDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ApprovalRouteDTO> save(ApprovalRouteDTO approvalRouteDTO);

    /**
     * Updates a approvalRoute.
     *
     * @param approvalRouteDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ApprovalRouteDTO> update(ApprovalRouteDTO approvalRouteDTO);

    /**
     * Partially updates a approvalRoute.
     *
     * @param approvalRouteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ApprovalRouteDTO> partialUpdate(ApprovalRouteDTO approvalRouteDTO);

    /**
     * Get all the approvalRoutes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ApprovalRouteDTO> findAll(Pageable pageable);

    /**
     * Get all the approvalRoutes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ApprovalRouteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of approvalRoutes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" approvalRoute.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ApprovalRouteDTO> findOne(String id);

    /**
     * Delete the "id" approvalRoute.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
