package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.PaymentBatchDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.PaymentBatch}.
 */
public interface PaymentBatchService {
    /**
     * Save a paymentBatch.
     *
     * @param paymentBatchDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PaymentBatchDTO> save(PaymentBatchDTO paymentBatchDTO);

    /**
     * Updates a paymentBatch.
     *
     * @param paymentBatchDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PaymentBatchDTO> update(PaymentBatchDTO paymentBatchDTO);

    /**
     * Partially updates a paymentBatch.
     *
     * @param paymentBatchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PaymentBatchDTO> partialUpdate(PaymentBatchDTO paymentBatchDTO);

    /**
     * Get all the paymentBatches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaymentBatchDTO> findAll(Pageable pageable);

    /**
     * Returns the number of paymentBatches available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" paymentBatch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PaymentBatchDTO> findOne(String id);

    /**
     * Delete the "id" paymentBatch.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
