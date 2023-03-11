package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.PaymentInstructionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.PaymentInstruction}.
 */
public interface PaymentInstructionService {
    /**
     * Save a paymentInstruction.
     *
     * @param paymentInstructionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PaymentInstructionDTO> save(PaymentInstructionDTO paymentInstructionDTO);

    /**
     * Updates a paymentInstruction.
     *
     * @param paymentInstructionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PaymentInstructionDTO> update(PaymentInstructionDTO paymentInstructionDTO);

    /**
     * Partially updates a paymentInstruction.
     *
     * @param paymentInstructionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PaymentInstructionDTO> partialUpdate(PaymentInstructionDTO paymentInstructionDTO);

    /**
     * Get all the paymentInstructions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaymentInstructionDTO> findAll(Pageable pageable);

    /**
     * Returns the number of paymentInstructions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" paymentInstruction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PaymentInstructionDTO> findOne(String id);

    /**
     * Delete the "id" paymentInstruction.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
