package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.ReconciliationItemRepository;
import com.simplifysynergy.service.ReconciliationItemService;
import com.simplifysynergy.service.dto.ReconciliationItemDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.ReconciliationItem}.
 */
@RestController
@RequestMapping("/api")
public class ReconciliationItemResource {

    private final Logger log = LoggerFactory.getLogger(ReconciliationItemResource.class);

    private static final String ENTITY_NAME = "reconciliationReconciliationItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReconciliationItemService reconciliationItemService;

    private final ReconciliationItemRepository reconciliationItemRepository;

    public ReconciliationItemResource(
        ReconciliationItemService reconciliationItemService,
        ReconciliationItemRepository reconciliationItemRepository
    ) {
        this.reconciliationItemService = reconciliationItemService;
        this.reconciliationItemRepository = reconciliationItemRepository;
    }

    /**
     * {@code POST  /reconciliation-items} : Create a new reconciliationItem.
     *
     * @param reconciliationItemDTO the reconciliationItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reconciliationItemDTO, or with status {@code 400 (Bad Request)} if the reconciliationItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reconciliation-items")
    public Mono<ResponseEntity<ReconciliationItemDTO>> createReconciliationItem(@RequestBody ReconciliationItemDTO reconciliationItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReconciliationItem : {}", reconciliationItemDTO);
        if (reconciliationItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new reconciliationItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return reconciliationItemService
            .save(reconciliationItemDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/reconciliation-items/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /reconciliation-items/:id} : Updates an existing reconciliationItem.
     *
     * @param id the id of the reconciliationItemDTO to save.
     * @param reconciliationItemDTO the reconciliationItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reconciliationItemDTO,
     * or with status {@code 400 (Bad Request)} if the reconciliationItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reconciliationItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reconciliation-items/{id}")
    public Mono<ResponseEntity<ReconciliationItemDTO>> updateReconciliationItem(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ReconciliationItemDTO reconciliationItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReconciliationItem : {}, {}", id, reconciliationItemDTO);
        if (reconciliationItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reconciliationItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reconciliationItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return reconciliationItemService
                    .update(reconciliationItemDTO)
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
     * {@code PATCH  /reconciliation-items/:id} : Partial updates given fields of an existing reconciliationItem, field will ignore if it is null
     *
     * @param id the id of the reconciliationItemDTO to save.
     * @param reconciliationItemDTO the reconciliationItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reconciliationItemDTO,
     * or with status {@code 400 (Bad Request)} if the reconciliationItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reconciliationItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reconciliationItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reconciliation-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReconciliationItemDTO>> partialUpdateReconciliationItem(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ReconciliationItemDTO reconciliationItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReconciliationItem partially : {}, {}", id, reconciliationItemDTO);
        if (reconciliationItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reconciliationItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reconciliationItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReconciliationItemDTO> result = reconciliationItemService.partialUpdate(reconciliationItemDTO);

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
     * {@code GET  /reconciliation-items} : get all the reconciliationItems.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reconciliationItems in body.
     */
    @GetMapping("/reconciliation-items")
    public Mono<ResponseEntity<List<ReconciliationItemDTO>>> getAllReconciliationItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ReconciliationItems");
        return reconciliationItemService
            .countAll()
            .zipWith(reconciliationItemService.findAll(pageable).collectList())
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
     * {@code GET  /reconciliation-items/:id} : get the "id" reconciliationItem.
     *
     * @param id the id of the reconciliationItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reconciliationItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reconciliation-items/{id}")
    public Mono<ResponseEntity<ReconciliationItemDTO>> getReconciliationItem(@PathVariable String id) {
        log.debug("REST request to get ReconciliationItem : {}", id);
        Mono<ReconciliationItemDTO> reconciliationItemDTO = reconciliationItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reconciliationItemDTO);
    }

    /**
     * {@code DELETE  /reconciliation-items/:id} : delete the "id" reconciliationItem.
     *
     * @param id the id of the reconciliationItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reconciliation-items/{id}")
    public Mono<ResponseEntity<Void>> deleteReconciliationItem(@PathVariable String id) {
        log.debug("REST request to delete ReconciliationItem : {}", id);
        return reconciliationItemService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
