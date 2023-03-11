package com.simplifysynergy.service;

import com.simplifysynergy.service.dto.ChartDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.simplifysynergy.domain.Chart}.
 */
public interface ChartService {
    /**
     * Save a chart.
     *
     * @param chartDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ChartDTO> save(ChartDTO chartDTO);

    /**
     * Updates a chart.
     *
     * @param chartDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ChartDTO> update(ChartDTO chartDTO);

    /**
     * Partially updates a chart.
     *
     * @param chartDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ChartDTO> partialUpdate(ChartDTO chartDTO);

    /**
     * Get all the charts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ChartDTO> findAll(Pageable pageable);

    /**
     * Returns the number of charts available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" chart.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ChartDTO> findOne(String id);

    /**
     * Delete the "id" chart.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
