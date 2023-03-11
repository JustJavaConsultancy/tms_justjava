package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.LeftItemRepository;
import com.simplifysynergy.service.LeftItemService;
import com.simplifysynergy.service.dto.LeftItemDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.LeftItem}.
 */
@RestController
@RequestMapping("/api")
public class LeftItemResource {

    private final Logger log = LoggerFactory.getLogger(LeftItemResource.class);

    private static final String ENTITY_NAME = "reconciliationLeftItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeftItemService leftItemService;

    private final LeftItemRepository leftItemRepository;

    public LeftItemResource(LeftItemService leftItemService, LeftItemRepository leftItemRepository) {
        this.leftItemService = leftItemService;
        this.leftItemRepository = leftItemRepository;
    }

    /**
     * {@code POST  /left-items} : Create a new leftItem.
     *
     * @param leftItemDTO the leftItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leftItemDTO, or with status {@code 400 (Bad Request)} if the leftItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/left-items")
    public Mono<ResponseEntity<LeftItemDTO>> createLeftItem(@RequestBody LeftItemDTO leftItemDTO) throws URISyntaxException {
        log.debug("REST request to save LeftItem : {}", leftItemDTO);
        if (leftItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new leftItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return leftItemService
            .save(leftItemDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/left-items/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /left-items/:id} : Updates an existing leftItem.
     *
     * @param id the id of the leftItemDTO to save.
     * @param leftItemDTO the leftItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leftItemDTO,
     * or with status {@code 400 (Bad Request)} if the leftItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leftItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/left-items/{id}")
    public Mono<ResponseEntity<LeftItemDTO>> updateLeftItem(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody LeftItemDTO leftItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeftItem : {}, {}", id, leftItemDTO);
        if (leftItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leftItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return leftItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return leftItemService
                    .update(leftItemDTO)
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
     * {@code PATCH  /left-items/:id} : Partial updates given fields of an existing leftItem, field will ignore if it is null
     *
     * @param id the id of the leftItemDTO to save.
     * @param leftItemDTO the leftItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leftItemDTO,
     * or with status {@code 400 (Bad Request)} if the leftItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leftItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leftItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/left-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LeftItemDTO>> partialUpdateLeftItem(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody LeftItemDTO leftItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeftItem partially : {}, {}", id, leftItemDTO);
        if (leftItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leftItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return leftItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LeftItemDTO> result = leftItemService.partialUpdate(leftItemDTO);

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
     * {@code GET  /left-items} : get all the leftItems.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leftItems in body.
     */
    @GetMapping("/left-items")
    public Mono<ResponseEntity<List<LeftItemDTO>>> getAllLeftItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of LeftItems");
        return leftItemService
            .countAll()
            .zipWith(leftItemService.findAll(pageable).collectList())
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
     * {@code GET  /left-items/:id} : get the "id" leftItem.
     *
     * @param id the id of the leftItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leftItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/left-items/{id}")
    public Mono<ResponseEntity<LeftItemDTO>> getLeftItem(@PathVariable String id) {
        log.debug("REST request to get LeftItem : {}", id);
        Mono<LeftItemDTO> leftItemDTO = leftItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leftItemDTO);
    }

    /**
     * {@code DELETE  /left-items/:id} : delete the "id" leftItem.
     *
     * @param id the id of the leftItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/left-items/{id}")
    public Mono<ResponseEntity<Void>> deleteLeftItem(@PathVariable String id) {
        log.debug("REST request to delete LeftItem : {}", id);
        return leftItemService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
