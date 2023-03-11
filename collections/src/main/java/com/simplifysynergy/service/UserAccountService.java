package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.UserAccountDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.UserAccount}.
 */
public interface UserAccountService {
    /**
     * Save a userAccount.
     *
     * @param userAccountDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserAccountDTO> save(UserAccountDTO userAccountDTO);

    /**
     * Updates a userAccount.
     *
     * @param userAccountDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserAccountDTO> update(UserAccountDTO userAccountDTO);

    /**
     * Partially updates a userAccount.
     *
     * @param userAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserAccountDTO> partialUpdate(UserAccountDTO userAccountDTO);

    /**
     * Get all the userAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserAccountDTO> findAll(Pageable pageable);

    /**
     * Returns the number of userAccounts available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" userAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserAccountDTO> findOne(String id);

    /**
     * Delete the "id" userAccount.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
