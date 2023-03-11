package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.ApprovalLineRepository;
import com.simplifysynergy.service.ApprovalLineService;
import com.simplifysynergy.service.dto.ApprovalLineDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.ApprovalLine}.
 */
@RestController
@RequestMapping("/api")
public class ApprovalLineResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalLineResource.class);

    private static final String ENTITY_NAME = "collectionsApprovalLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprovalLineService approvalLineService;

    private final ApprovalLineRepository approvalLineRepository;

    public ApprovalLineResource(ApprovalLineService approvalLineService, ApprovalLineRepository approvalLineRepository) {
        this.approvalLineService = approvalLineService;
        this.approvalLineRepository = approvalLineRepository;
    }

    /**
     * {@code POST  /approval-lines} : Create a new approvalLine.
     *
     * @param approvalLineDTO the approvalLineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvalLineDTO, or with status {@code 400 (Bad Request)} if the approvalLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approval-lines")
    public Mono<ResponseEntity<ApprovalLineDTO>> createApprovalLine(@RequestBody ApprovalLineDTO approvalLineDTO)
        throws URISyntaxException {
        log.debug("REST request to save ApprovalLine : {}", approvalLineDTO);
        if (approvalLineDTO.getId() != null) {
            throw new BadRequestAlertException("A new approvalLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return approvalLineService
            .save(approvalLineDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/approval-lines/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /approval-lines/:id} : Updates an existing approvalLine.
     *
     * @param id the id of the approvalLineDTO to save.
     * @param approvalLineDTO the approvalLineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalLineDTO,
     * or with status {@code 400 (Bad Request)} if the approvalLineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvalLineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/approval-lines/{id}")
    public Mono<ResponseEntity<ApprovalLineDTO>> updateApprovalLine(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ApprovalLineDTO approvalLineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ApprovalLine : {}, {}", id, approvalLineDTO);
        if (approvalLineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalLineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return approvalLineRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return approvalLineService
                    .update(approvalLineDTO)
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
     * {@code PATCH  /approval-lines/:id} : Partial updates given fields of an existing approvalLine, field will ignore if it is null
     *
     * @param id the id of the approvalLineDTO to save.
     * @param approvalLineDTO the approvalLineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalLineDTO,
     * or with status {@code 400 (Bad Request)} if the approvalLineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the approvalLineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the approvalLineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/approval-lines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ApprovalLineDTO>> partialUpdateApprovalLine(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ApprovalLineDTO approvalLineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApprovalLine partially : {}, {}", id, approvalLineDTO);
        if (approvalLineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalLineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return approvalLineRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ApprovalLineDTO> result = approvalLineService.partialUpdate(approvalLineDTO);

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
     * {@code GET  /approval-lines} : get all the approvalLines.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvalLines in body.
     */
    @GetMapping("/approval-lines")
    public Mono<ResponseEntity<List<ApprovalLineDTO>>> getAllApprovalLines(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ApprovalLines");
        return approvalLineService
            .countAll()
            .zipWith(approvalLineService.findAll(pageable).collectList())
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
     * {@code GET  /approval-lines/:id} : get the "id" approvalLine.
     *
     * @param id the id of the approvalLineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvalLineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/approval-lines/{id}")
    public Mono<ResponseEntity<ApprovalLineDTO>> getApprovalLine(@PathVariable String id) {
        log.debug("REST request to get ApprovalLine : {}", id);
        Mono<ApprovalLineDTO> approvalLineDTO = approvalLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(approvalLineDTO);
    }

    /**
     * {@code DELETE  /approval-lines/:id} : delete the "id" approvalLine.
     *
     * @param id the id of the approvalLineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/approval-lines/{id}")
    public Mono<ResponseEntity<Void>> deleteApprovalLine(@PathVariable String id) {
        log.debug("REST request to delete ApprovalLine : {}", id);
        return approvalLineService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
