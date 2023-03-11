package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.RequestContentRepository;
import com.simplifysynergy.service.RequestContentService;
import com.simplifysynergy.service.dto.RequestContentDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.RequestContent}.
 */
@RestController
@RequestMapping("/api")
public class RequestContentResource {

    private final Logger log = LoggerFactory.getLogger(RequestContentResource.class);

    private static final String ENTITY_NAME = "paymentRequestContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestContentService requestContentService;

    private final RequestContentRepository requestContentRepository;

    public RequestContentResource(RequestContentService requestContentService, RequestContentRepository requestContentRepository) {
        this.requestContentService = requestContentService;
        this.requestContentRepository = requestContentRepository;
    }

    /**
     * {@code POST  /request-contents} : Create a new requestContent.
     *
     * @param requestContentDTO the requestContentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestContentDTO, or with status {@code 400 (Bad Request)} if the requestContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/request-contents")
    public Mono<ResponseEntity<RequestContentDTO>> createRequestContent(@RequestBody RequestContentDTO requestContentDTO)
        throws URISyntaxException {
        log.debug("REST request to save RequestContent : {}", requestContentDTO);
        if (requestContentDTO.getId() != null) {
            throw new BadRequestAlertException("A new requestContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return requestContentService
            .save(requestContentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/request-contents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /request-contents/:id} : Updates an existing requestContent.
     *
     * @param id the id of the requestContentDTO to save.
     * @param requestContentDTO the requestContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestContentDTO,
     * or with status {@code 400 (Bad Request)} if the requestContentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/request-contents/{id}")
    public Mono<ResponseEntity<RequestContentDTO>> updateRequestContent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RequestContentDTO requestContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RequestContent : {}, {}", id, requestContentDTO);
        if (requestContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return requestContentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return requestContentService
                    .update(requestContentDTO)
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
     * {@code PATCH  /request-contents/:id} : Partial updates given fields of an existing requestContent, field will ignore if it is null
     *
     * @param id the id of the requestContentDTO to save.
     * @param requestContentDTO the requestContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestContentDTO,
     * or with status {@code 400 (Bad Request)} if the requestContentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the requestContentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the requestContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/request-contents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RequestContentDTO>> partialUpdateRequestContent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RequestContentDTO requestContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RequestContent partially : {}, {}", id, requestContentDTO);
        if (requestContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return requestContentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RequestContentDTO> result = requestContentService.partialUpdate(requestContentDTO);

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
     * {@code GET  /request-contents} : get all the requestContents.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requestContents in body.
     */
    @GetMapping("/request-contents")
    public Mono<ResponseEntity<List<RequestContentDTO>>> getAllRequestContents(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of RequestContents");
        return requestContentService
            .countAll()
            .zipWith(requestContentService.findAll(pageable).collectList())
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
     * {@code GET  /request-contents/:id} : get the "id" requestContent.
     *
     * @param id the id of the requestContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/request-contents/{id}")
    public Mono<ResponseEntity<RequestContentDTO>> getRequestContent(@PathVariable String id) {
        log.debug("REST request to get RequestContent : {}", id);
        Mono<RequestContentDTO> requestContentDTO = requestContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestContentDTO);
    }

    /**
     * {@code DELETE  /request-contents/:id} : delete the "id" requestContent.
     *
     * @param id the id of the requestContentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/request-contents/{id}")
    public Mono<ResponseEntity<Void>> deleteRequestContent(@PathVariable String id) {
        log.debug("REST request to delete RequestContent : {}", id);
        return requestContentService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
