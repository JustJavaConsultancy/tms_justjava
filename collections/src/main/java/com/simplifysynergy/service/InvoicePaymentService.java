package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.InvoicePaymentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.InvoicePayment}.
 */
public interface InvoicePaymentService {
    /**
     * Save a invoicePayment.
     *
     * @param invoicePaymentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InvoicePaymentDTO> save(InvoicePaymentDTO invoicePaymentDTO);

    /**
     * Updates a invoicePayment.
     *
     * @param invoicePaymentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InvoicePaymentDTO> update(InvoicePaymentDTO invoicePaymentDTO);

    /**
     * Partially updates a invoicePayment.
     *
     * @param invoicePaymentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InvoicePaymentDTO> partialUpdate(InvoicePaymentDTO invoicePaymentDTO);

    /**
     * Get all the invoicePayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InvoicePaymentDTO> findAll(Pageable pageable);

    /**
     * Returns the number of invoicePayments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" invoicePayment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InvoicePaymentDTO> findOne(String id);

    /**
     * Delete the "id" invoicePayment.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
