package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.ChartOfAccountRepository;
import com.simplifysynergy.service.ChartOfAccountService;
import com.simplifysynergy.service.dto.ChartOfAccountDTO;
import com.simplifysynergy.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.simplifysynergy.domain.ChartOfAccount}.
 */
@RestController
@RequestMapping("/api")
public class ChartOfAccountResource {

    private final Logger log = LoggerFactory.getLogger(ChartOfAccountResource.class);

    private static final String ENTITY_NAME = "collectionsChartOfAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChartOfAccountService chartOfAccountService;

    private final ChartOfAccountRepository chartOfAccountRepository;

    public ChartOfAccountResource(ChartOfAccountService chartOfAccountService, ChartOfAccountRepository chartOfAccountRepository) {
        this.chartOfAccountService = chartOfAccountService;
        this.chartOfAccountRepository = chartOfAccountRepository;
    }

    /**
     * {@code POST  /chart-of-accounts} : Create a new chartOfAccount.
     *
     * @param chartOfAccountDTO the chartOfAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chartOfAccountDTO, or with status {@code 400 (Bad Request)} if the chartOfAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chart-of-accounts")
    public Mono<ResponseEntity<ChartOfAccountDTO>> createChartOfAccount(@RequestBody ChartOfAccountDTO chartOfAccountDTO)
        throws URISyntaxException {
        log.debug("REST request to save ChartOfAccount : {}", chartOfAccountDTO);
        if (chartOfAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new chartOfAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return chartOfAccountService
            .save(chartOfAccountDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/chart-of-accounts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /chart-of-accounts/:id} : Updates an existing chartOfAccount.
     *
     * @param id the id of the chartOfAccountDTO to save.
     * @param chartOfAccountDTO the chartOfAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chartOfAccountDTO,
     * or with status {@code 400 (Bad Request)} if the chartOfAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chartOfAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chart-of-accounts/{id}")
    public Mono<ResponseEntity<ChartOfAccountDTO>> updateChartOfAccount(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ChartOfAccountDTO chartOfAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ChartOfAccount : {}, {}", id, chartOfAccountDTO);
        if (chartOfAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chartOfAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return chartOfAccountRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return chartOfAccountService
                    .update(chartOfAccountDTO)
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
     * {@code PATCH  /chart-of-accounts/:id} : Partial updates given fields of an existing chartOfAccount, field will ignore if it is null
     *
     * @param id the id of the chartOfAccountDTO to save.
     * @param chartOfAccountDTO the chartOfAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chartOfAccountDTO,
     * or with status {@code 400 (Bad Request)} if the chartOfAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chartOfAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chartOfAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chart-of-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ChartOfAccountDTO>> partialUpdateChartOfAccount(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ChartOfAccountDTO chartOfAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChartOfAccount partially : {}, {}", id, chartOfAccountDTO);
        if (chartOfAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chartOfAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return chartOfAccountRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ChartOfAccountDTO> result = chartOfAccountService.partialUpdate(chartOfAccountDTO);

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
     * {@code GET  /chart-of-accounts} : get all the chartOfAccounts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chartOfAccounts in body.
     */
    @GetMapping("/chart-of-accounts")
    public Mono<ResponseEntity<List<ChartOfAccountDTO>>> getAllChartOfAccounts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ChartOfAccounts");
        return chartOfAccountService
            .countAll()
            .zipWith(chartOfAccountService.findAll(pageable).collectList())
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
     * {@code GET  /chart-of-accounts/:id} : get the "id" chartOfAccount.
     *
     * @param id the id of the chartOfAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chartOfAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chart-of-accounts/{id}")
    public Mono<ResponseEntity<ChartOfAccountDTO>> getChartOfAccount(@PathVariable String id) {
        log.debug("REST request to get ChartOfAccount : {}", id);
        Mono<ChartOfAccountDTO> chartOfAccountDTO = chartOfAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chartOfAccountDTO);
    }

    /**
     * {@code DELETE  /chart-of-accounts/:id} : delete the "id" chartOfAccount.
     *
     * @param id the id of the chartOfAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chart-of-accounts/{id}")
    public Mono<ResponseEntity<Void>> deleteChartOfAccount(@PathVariable String id) {
        log.debug("REST request to delete ChartOfAccount : {}", id);
        return chartOfAccountService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
