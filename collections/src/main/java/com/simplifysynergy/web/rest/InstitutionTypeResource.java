package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.InstitutionTypeRepository;
import com.simplifysynergy.service.InstitutionTypeService;
import com.simplifysynergy.service.dto.InstitutionTypeDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.InstitutionType}.
 */
@RestController
@RequestMapping("/api")
public class InstitutionTypeResource {

    private final Logger log = LoggerFactory.getLogger(InstitutionTypeResource.class);

    private static final String ENTITY_NAME = "collectionsInstitutionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstitutionTypeService institutionTypeService;

    private final InstitutionTypeRepository institutionTypeRepository;

    public InstitutionTypeResource(InstitutionTypeService institutionTypeService, InstitutionTypeRepository institutionTypeRepository) {
        this.institutionTypeService = institutionTypeService;
        this.institutionTypeRepository = institutionTypeRepository;
    }

    /**
     * {@code POST  /institution-types} : Create a new institutionType.
     *
     * @param institutionTypeDTO the institutionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new institutionTypeDTO, or with status {@code 400 (Bad Request)} if the institutionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/institution-types")
    public Mono<ResponseEntity<InstitutionTypeDTO>> createInstitutionType(@Valid @RequestBody InstitutionTypeDTO institutionTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save InstitutionType : {}", institutionTypeDTO);
        if (institutionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new institutionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return institutionTypeService
            .save(institutionTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/institution-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /institution-types/:id} : Updates an existing institutionType.
     *
     * @param id the id of the institutionTypeDTO to save.
     * @param institutionTypeDTO the institutionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institutionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the institutionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the institutionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/institution-types/{id}")
    public Mono<ResponseEntity<InstitutionTypeDTO>> updateInstitutionType(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody InstitutionTypeDTO institutionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InstitutionType : {}, {}", id, institutionTypeDTO);
        if (institutionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institutionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return institutionTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return institutionTypeService
                    .update(institutionTypeDTO)
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
     * {@code PATCH  /institution-types/:id} : Partial updates given fields of an existing institutionType, field will ignore if it is null
     *
     * @param id the id of the institutionTypeDTO to save.
     * @param institutionTypeDTO the institutionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institutionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the institutionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the institutionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the institutionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/institution-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InstitutionTypeDTO>> partialUpdateInstitutionType(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody InstitutionTypeDTO institutionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InstitutionType partially : {}, {}", id, institutionTypeDTO);
        if (institutionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institutionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return institutionTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InstitutionTypeDTO> result = institutionTypeService.partialUpdate(institutionTypeDTO);

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
     * {@code GET  /institution-types} : get all the institutionTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of institutionTypes in body.
     */
    @GetMapping("/institution-types")
    public Mono<ResponseEntity<List<InstitutionTypeDTO>>> getAllInstitutionTypes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of InstitutionTypes");
        return institutionTypeService
            .countAll()
            .zipWith(institutionTypeService.findAll(pageable).collectList())
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
     * {@code GET  /institution-types/:id} : get the "id" institutionType.
     *
     * @param id the id of the institutionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the institutionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/institution-types/{id}")
    public Mono<ResponseEntity<InstitutionTypeDTO>> getInstitutionType(@PathVariable String id) {
        log.debug("REST request to get InstitutionType : {}", id);
        Mono<InstitutionTypeDTO> institutionTypeDTO = institutionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(institutionTypeDTO);
    }

    /**
     * {@code DELETE  /institution-types/:id} : delete the "id" institutionType.
     *
     * @param id the id of the institutionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/institution-types/{id}")
    public Mono<ResponseEntity<Void>> deleteInstitutionType(@PathVariable String id) {
        log.debug("REST request to delete InstitutionType : {}", id);
        return institutionTypeService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
