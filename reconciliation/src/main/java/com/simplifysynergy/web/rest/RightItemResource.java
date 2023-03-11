package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.RightItemRepository;
import com.simplifysynergy.service.RightItemService;
import com.simplifysynergy.service.dto.RightItemDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.RightItem}.
 */
@RestController
@RequestMapping("/api")
public class RightItemResource {

    private final Logger log = LoggerFactory.getLogger(RightItemResource.class);

    private static final String ENTITY_NAME = "reconciliationRightItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RightItemService rightItemService;

    private final RightItemRepository rightItemRepository;

    public RightItemResource(RightItemService rightItemService, RightItemRepository rightItemRepository) {
        this.rightItemService = rightItemService;
        this.rightItemRepository = rightItemRepository;
    }

    /**
     * {@code POST  /right-items} : Create a new rightItem.
     *
     * @param rightItemDTO the rightItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rightItemDTO, or with status {@code 400 (Bad Request)} if the rightItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/right-items")
    public Mono<ResponseEntity<RightItemDTO>> createRightItem(@RequestBody RightItemDTO rightItemDTO) throws URISyntaxException {
        log.debug("REST request to save RightItem : {}", rightItemDTO);
        if (rightItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new rightItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return rightItemService
            .save(rightItemDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/right-items/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /right-items/:id} : Updates an existing rightItem.
     *
     * @param id the id of the rightItemDTO to save.
     * @param rightItemDTO the rightItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rightItemDTO,
     * or with status {@code 400 (Bad Request)} if the rightItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rightItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/right-items/{id}")
    public Mono<ResponseEntity<RightItemDTO>> updateRightItem(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RightItemDTO rightItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RightItem : {}, {}", id, rightItemDTO);
        if (rightItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rightItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rightItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return rightItemService
                    .update(rightItemDTO)
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
     * {@code PATCH  /right-items/:id} : Partial updates given fields of an existing rightItem, field will ignore if it is null
     *
     * @param id the id of the rightItemDTO to save.
     * @param rightItemDTO the rightItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rightItemDTO,
     * or with status {@code 400 (Bad Request)} if the rightItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rightItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rightItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/right-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RightItemDTO>> partialUpdateRightItem(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RightItemDTO rightItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RightItem partially : {}, {}", id, rightItemDTO);
        if (rightItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rightItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rightItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RightItemDTO> result = rightItemService.partialUpdate(rightItemDTO);

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
     * {@code GET  /right-items} : get all the rightItems.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rightItems in body.
     */
    @GetMapping("/right-items")
    public Mono<ResponseEntity<List<RightItemDTO>>> getAllRightItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of RightItems");
        return rightItemService
            .countAll()
            .zipWith(rightItemService.findAll(pageable).collectList())
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
     * {@code GET  /right-items/:id} : get the "id" rightItem.
     *
     * @param id the id of the rightItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rightItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/right-items/{id}")
    public Mono<ResponseEntity<RightItemDTO>> getRightItem(@PathVariable String id) {
        log.debug("REST request to get RightItem : {}", id);
        Mono<RightItemDTO> rightItemDTO = rightItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rightItemDTO);
    }

    /**
     * {@code DELETE  /right-items/:id} : delete the "id" rightItem.
     *
     * @param id the id of the rightItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/right-items/{id}")
    public Mono<ResponseEntity<Void>> deleteRightItem(@PathVariable String id) {
        log.debug("REST request to delete RightItem : {}", id);
        return rightItemService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
