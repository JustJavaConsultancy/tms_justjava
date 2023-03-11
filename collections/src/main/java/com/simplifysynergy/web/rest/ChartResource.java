package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.ChartRepository;
import com.simplifysynergy.service.ChartService;
import com.simplifysynergy.service.dto.ChartDTO;
import com.simplifysynergy.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.simplifysynergy.domain.Chart}.
 */
@RestController
@RequestMapping("/api")
public class ChartResource {

    private final Logger log = LoggerFactory.getLogger(ChartResource.class);

    private static final String ENTITY_NAME = "collectionsChart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChartService chartService;

    private final ChartRepository chartRepository;

    public ChartResource(ChartService chartService, ChartRepository chartRepository) {
        this.chartService = chartService;
        this.chartRepository = chartRepository;
    }

    /**
     * {@code POST  /charts} : Create a new chart.
     *
     * @param chartDTO the chartDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chartDTO, or with status {@code 400 (Bad Request)} if the chart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/charts")
    public Mono<ResponseEntity<ChartDTO>> createChart(@Valid @RequestBody ChartDTO chartDTO) throws URISyntaxException {
        log.debug("REST request to save Chart : {}", chartDTO);
        if (chartDTO.getId() != null) {
            throw new BadRequestAlertException("A new chart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return chartService
            .save(chartDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/charts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /charts/:id} : Updates an existing chart.
     *
     * @param id the id of the chartDTO to save.
     * @param chartDTO the chartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chartDTO,
     * or with status {@code 400 (Bad Request)} if the chartDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/charts/{id}")
    public Mono<ResponseEntity<ChartDTO>> updateChart(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ChartDTO chartDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Chart : {}, {}", id, chartDTO);
        if (chartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return chartRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return chartService
                    .update(chartDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /charts/:id} : Partial updates given fields of an existing chart, field will ignore if it is null
     *
     * @param id the id of the chartDTO to save.
     * @param chartDTO the chartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chartDTO,
     * or with status {@code 400 (Bad Request)} if the chartDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chartDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/charts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ChartDTO>> partialUpdateChart(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ChartDTO chartDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Chart partially : {}, {}", id, chartDTO);
        if (chartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return chartRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ChartDTO> result = chartService.partialUpdate(chartDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /charts} : get all the charts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of charts in body.
     */
    @GetMapping("/charts")
    public Mono<ResponseEntity<List<ChartDTO>>> getAllCharts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Charts");
        return chartService
            .countAll()
            .zipWith(chartService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /charts/:id} : get the "id" chart.
     *
     * @param id the id of the chartDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chartDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/charts/{id}")
    public Mono<ResponseEntity<ChartDTO>> getChart(@PathVariable String id) {
        log.debug("REST request to get Chart : {}", id);
        Mono<ChartDTO> chartDTO = chartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chartDTO);
    }

    /**
     * {@code DELETE  /charts/:id} : delete the "id" chart.
     *
     * @param id the id of the chartDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/charts/{id}")
    public Mono<ResponseEntity<Void>> deleteChart(@PathVariable String id) {
        log.debug("REST request to delete Chart : {}", id);
        return chartService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
