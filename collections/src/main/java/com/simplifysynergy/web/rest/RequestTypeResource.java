package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.RequestTypeRepository;
import com.simplifysynergy.service.RequestTypeService;
import com.simplifysynergy.service.dto.RequestTypeDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.RequestType}.
 */
@RestController
@RequestMapping("/api")
public class RequestTypeResource {

    private final Logger log = LoggerFactory.getLogger(RequestTypeResource.class);

    private static final String ENTITY_NAME = "collectionsRequestType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestTypeService requestTypeService;

    private final RequestTypeRepository requestTypeRepository;

    public RequestTypeResource(RequestTypeService requestTypeService, RequestTypeRepository requestTypeRepository) {
        this.requestTypeService = requestTypeService;
        this.requestTypeRepository = requestTypeRepository;
    }

    /**
     * {@code POST  /request-types} : Create a new requestType.
     *
     * @param requestTypeDTO the requestTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestTypeDTO, or with status {@code 400 (Bad Request)} if the requestType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/request-types")
    public Mono<ResponseEntity<RequestTypeDTO>> createRequestType(@Valid @RequestBody RequestTypeDTO requestTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save RequestType : {}", requestTypeDTO);
        if (requestTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new requestType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return requestTypeService
            .save(requestTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/request-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /request-types/:id} : Updates an existing requestType.
     *
     * @param id the id of the requestTypeDTO to save.
     * @param requestTypeDTO the requestTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestTypeDTO,
     * or with status {@code 400 (Bad Request)} if the requestTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/request-types/{id}")
    public Mono<ResponseEntity<RequestTypeDTO>> updateRequestType(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody RequestTypeDTO requestTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RequestType : {}, {}", id, requestTypeDTO);
        if (requestTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return requestTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return requestTypeService
                    .update(requestTypeDTO)
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
     * {@code PATCH  /request-types/:id} : Partial updates given fields of an existing requestType, field will ignore if it is null
     *
     * @param id the id of the requestTypeDTO to save.
     * @param requestTypeDTO the requestTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestTypeDTO,
     * or with status {@code 400 (Bad Request)} if the requestTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the requestTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the requestTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/request-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RequestTypeDTO>> partialUpdateRequestType(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody RequestTypeDTO requestTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RequestType partially : {}, {}", id, requestTypeDTO);
        if (requestTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return requestTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RequestTypeDTO> result = requestTypeService.partialUpdate(requestTypeDTO);

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
     * {@code GET  /request-types} : get all the requestTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requestTypes in body.
     */
    @GetMapping("/request-types")
    public Mono<ResponseEntity<List<RequestTypeDTO>>> getAllRequestTypes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of RequestTypes");
        return requestTypeService
            .countAll()
            .zipWith(requestTypeService.findAll(pageable).collectList())
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
     * {@code GET  /request-types/:id} : get the "id" requestType.
     *
     * @param id the id of the requestTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/request-types/{id}")
    public Mono<ResponseEntity<RequestTypeDTO>> getRequestType(@PathVariable String id) {
        log.debug("REST request to get RequestType : {}", id);
        Mono<RequestTypeDTO> requestTypeDTO = requestTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestTypeDTO);
    }

    /**
     * {@code DELETE  /request-types/:id} : delete the "id" requestType.
     *
     * @param id the id of the requestTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/request-types/{id}")
    public Mono<ResponseEntity<Void>> deleteRequestType(@PathVariable String id) {
        log.debug("REST request to delete RequestType : {}", id);
        return requestTypeService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
