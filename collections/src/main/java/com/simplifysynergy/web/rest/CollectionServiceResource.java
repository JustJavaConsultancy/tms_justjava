package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.CollectionServiceRepository;
import com.simplifysynergy.service.CollectionServiceService;
import com.simplifysynergy.service.dto.CollectionServiceDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.CollectionService}.
 */
@RestController
@RequestMapping("/api")
public class CollectionServiceResource {

    private final Logger log = LoggerFactory.getLogger(CollectionServiceResource.class);

    private static final String ENTITY_NAME = "collectionsCollectionService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollectionServiceService collectionServiceService;

    private final CollectionServiceRepository collectionServiceRepository;

    public CollectionServiceResource(
        CollectionServiceService collectionServiceService,
        CollectionServiceRepository collectionServiceRepository
    ) {
        this.collectionServiceService = collectionServiceService;
        this.collectionServiceRepository = collectionServiceRepository;
    }

    /**
     * {@code POST  /collection-services} : Create a new collectionService.
     *
     * @param collectionServiceDTO the collectionServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collectionServiceDTO, or with status {@code 400 (Bad Request)} if the collectionService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collection-services")
    public Mono<ResponseEntity<CollectionServiceDTO>> createCollectionService(@RequestBody CollectionServiceDTO collectionServiceDTO)
        throws URISyntaxException {
        log.debug("REST request to save CollectionService : {}", collectionServiceDTO);
        if (collectionServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new collectionService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return collectionServiceService
            .save(collectionServiceDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/collection-services/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /collection-services/:id} : Updates an existing collectionService.
     *
     * @param id the id of the collectionServiceDTO to save.
     * @param collectionServiceDTO the collectionServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectionServiceDTO,
     * or with status {@code 400 (Bad Request)} if the collectionServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collectionServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collection-services/{id}")
    public Mono<ResponseEntity<CollectionServiceDTO>> updateCollectionService(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CollectionServiceDTO collectionServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CollectionService : {}, {}", id, collectionServiceDTO);
        if (collectionServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collectionServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return collectionServiceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return collectionServiceService
                    .update(collectionServiceDTO)
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
     * {@code PATCH  /collection-services/:id} : Partial updates given fields of an existing collectionService, field will ignore if it is null
     *
     * @param id the id of the collectionServiceDTO to save.
     * @param collectionServiceDTO the collectionServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectionServiceDTO,
     * or with status {@code 400 (Bad Request)} if the collectionServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the collectionServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the collectionServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/collection-services/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CollectionServiceDTO>> partialUpdateCollectionService(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CollectionServiceDTO collectionServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CollectionService partially : {}, {}", id, collectionServiceDTO);
        if (collectionServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collectionServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return collectionServiceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CollectionServiceDTO> result = collectionServiceService.partialUpdate(collectionServiceDTO);

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
     * {@code GET  /collection-services} : get all the collectionServices.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collectionServices in body.
     */
    @GetMapping("/collection-services")
    public Mono<ResponseEntity<List<CollectionServiceDTO>>> getAllCollectionServices(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of CollectionServices");
        return collectionServiceService
            .countAll()
            .zipWith(collectionServiceService.findAll(pageable).collectList())
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
     * {@code GET  /collection-services/:id} : get the "id" collectionService.
     *
     * @param id the id of the collectionServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collectionServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collection-services/{id}")
    public Mono<ResponseEntity<CollectionServiceDTO>> getCollectionService(@PathVariable String id) {
        log.debug("REST request to get CollectionService : {}", id);
        Mono<CollectionServiceDTO> collectionServiceDTO = collectionServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collectionServiceDTO);
    }

    /**
     * {@code DELETE  /collection-services/:id} : delete the "id" collectionService.
     *
     * @param id the id of the collectionServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collection-services/{id}")
    public Mono<ResponseEntity<Void>> deleteCollectionService(@PathVariable String id) {
        log.debug("REST request to delete CollectionService : {}", id);
        return collectionServiceService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
