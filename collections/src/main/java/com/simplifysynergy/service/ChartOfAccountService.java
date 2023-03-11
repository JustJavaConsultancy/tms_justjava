package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.ChartOfAccountDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.ChartOfAccount}.
 */
public interface ChartOfAccountService {
    /**
     * Save a chartOfAccount.
     *
     * @param chartOfAccountDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ChartOfAccountDTO> save(ChartOfAccountDTO chartOfAccountDTO);

    /**
     * Updates a chartOfAccount.
     *
     * @param chartOfAccountDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ChartOfAccountDTO> update(ChartOfAccountDTO chartOfAccountDTO);

    /**
     * Partially updates a chartOfAccount.
     *
     * @param chartOfAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ChartOfAccountDTO> partialUpdate(ChartOfAccountDTO chartOfAccountDTO);

    /**
     * Get all the chartOfAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ChartOfAccountDTO> findAll(Pageable pageable);

    /**
     * Returns the number of chartOfAccounts available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" chartOfAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ChartOfAccountDTO> findOne(String id);

    /**
     * Delete the "id" chartOfAccount.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
