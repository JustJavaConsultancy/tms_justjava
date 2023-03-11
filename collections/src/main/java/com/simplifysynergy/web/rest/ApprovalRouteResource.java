package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.ApprovalRouteRepository;
import com.simplifysynergy.service.ApprovalRouteService;
import com.simplifysynergy.service.dto.ApprovalRouteDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.ApprovalRoute}.
 */
@RestController
@RequestMapping("/api")
public class ApprovalRouteResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalRouteResource.class);

    private static final String ENTITY_NAME = "collectionsApprovalRoute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprovalRouteService approvalRouteService;

    private final ApprovalRouteRepository approvalRouteRepository;

    public ApprovalRouteResource(ApprovalRouteService approvalRouteService, ApprovalRouteRepository approvalRouteRepository) {
        this.approvalRouteService = approvalRouteService;
        this.approvalRouteRepository = approvalRouteRepository;
    }

    /**
     * {@code POST  /approval-routes} : Create a new approvalRoute.
     *
     * @param approvalRouteDTO the approvalRouteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvalRouteDTO, or with status {@code 400 (Bad Request)} if the approvalRoute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approval-routes")
    public Mono<ResponseEntity<ApprovalRouteDTO>> createApprovalRoute(@Valid @RequestBody ApprovalRouteDTO approvalRouteDTO)
        throws URISyntaxException {
        log.debug("REST request to save ApprovalRoute : {}", approvalRouteDTO);
        if (approvalRouteDTO.getId() != null) {
            throw new BadRequestAlertException("A new approvalRoute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return approvalRouteService
            .save(approvalRouteDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/approval-routes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /approval-routes/:id} : Updates an existing approvalRoute.
     *
     * @param id the id of the approvalRouteDTO to save.
     * @param approvalRouteDTO the approvalRouteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalRouteDTO,
     * or with status {@code 400 (Bad Request)} if the approvalRouteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvalRouteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/approval-routes/{id}")
    public Mono<ResponseEntity<ApprovalRouteDTO>> updateApprovalRoute(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ApprovalRouteDTO approvalRouteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ApprovalRoute : {}, {}", id, approvalRouteDTO);
        if (approvalRouteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalRouteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return approvalRouteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return approvalRouteService
                    .update(approvalRouteDTO)
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
     * {@code PATCH  /approval-routes/:id} : Partial updates given fields of an existing approvalRoute, field will ignore if it is null
     *
     * @param id the id of the approvalRouteDTO to save.
     * @param approvalRouteDTO the approvalRouteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalRouteDTO,
     * or with status {@code 400 (Bad Request)} if the approvalRouteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the approvalRouteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the approvalRouteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/approval-routes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ApprovalRouteDTO>> partialUpdateApprovalRoute(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ApprovalRouteDTO approvalRouteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApprovalRoute partially : {}, {}", id, approvalRouteDTO);
        if (approvalRouteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalRouteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return approvalRouteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ApprovalRouteDTO> result = approvalRouteService.partialUpdate(approvalRouteDTO);

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
     * {@code GET  /approval-routes} : get all the approvalRoutes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvalRoutes in body.
     */
    @GetMapping("/approval-routes")
    public Mono<ResponseEntity<List<ApprovalRouteDTO>>> getAllApprovalRoutes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of ApprovalRoutes");
        return approvalRouteService
            .countAll()
            .zipWith(approvalRouteService.findAll(pageable).collectList())
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
     * {@code GET  /approval-routes/:id} : get the "id" approvalRoute.
     *
     * @param id the id of the approvalRouteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvalRouteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/approval-routes/{id}")
    public Mono<ResponseEntity<ApprovalRouteDTO>> getApprovalRoute(@PathVariable String id) {
        log.debug("REST request to get ApprovalRoute : {}", id);
        Mono<ApprovalRouteDTO> approvalRouteDTO = approvalRouteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(approvalRouteDTO);
    }

    /**
     * {@code DELETE  /approval-routes/:id} : delete the "id" approvalRoute.
     *
     * @param id the id of the approvalRouteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/approval-routes/{id}")
    public Mono<ResponseEntity<Void>> deleteApprovalRoute(@PathVariable String id) {
        log.debug("REST request to delete ApprovalRoute : {}", id);
        return approvalRouteService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
