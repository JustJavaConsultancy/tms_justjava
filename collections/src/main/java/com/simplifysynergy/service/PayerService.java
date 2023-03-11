package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.PayerDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.Payer}.
 */
public interface PayerService {
    /**
     * Save a payer.
     *
     * @param payerDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PayerDTO> save(PayerDTO payerDTO);

    /**
     * Updates a payer.
     *
     * @param payerDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PayerDTO> update(PayerDTO payerDTO);

    /**
     * Partially updates a payer.
     *
     * @param payerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PayerDTO> partialUpdate(PayerDTO payerDTO);

    /**
     * Get all the payers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PayerDTO> findAll(Pageable pageable);

    /**
     * Returns the number of payers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" payer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PayerDTO> findOne(String id);

    /**
     * Delete the "id" payer.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
