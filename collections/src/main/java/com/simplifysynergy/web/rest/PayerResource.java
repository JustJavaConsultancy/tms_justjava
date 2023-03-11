package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.PayerRepository;
import com.simplifysynergy.service.PayerService;
import com.simplifysynergy.service.dto.PayerDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.Payer}.
 */
@RestController
@RequestMapping("/api")
public class PayerResource {

    private final Logger log = LoggerFactory.getLogger(PayerResource.class);

    private static final String ENTITY_NAME = "collectionsPayer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayerService payerService;

    private final PayerRepository payerRepository;

    public PayerResource(PayerService payerService, PayerRepository payerRepository) {
        this.payerService = payerService;
        this.payerRepository = payerRepository;
    }

    /**
     * {@code POST  /payers} : Create a new payer.
     *
     * @param payerDTO the payerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payerDTO, or with status {@code 400 (Bad Request)} if the payer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payers")
    public Mono<ResponseEntity<PayerDTO>> createPayer(@RequestBody PayerDTO payerDTO) throws URISyntaxException {
        log.debug("REST request to save Payer : {}", payerDTO);
        if (payerDTO.getId() != null) {
            throw new BadRequestAlertException("A new payer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return payerService
            .save(payerDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/payers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /payers/:id} : Updates an existing payer.
     *
     * @param id the id of the payerDTO to save.
     * @param payerDTO the payerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payerDTO,
     * or with status {@code 400 (Bad Request)} if the payerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payers/{id}")
    public Mono<ResponseEntity<PayerDTO>> updatePayer(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PayerDTO payerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Payer : {}, {}", id, payerDTO);
        if (payerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return payerRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return payerService
                    .update(payerDTO)
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
     * {@code PATCH  /payers/:id} : Partial updates given fields of an existing payer, field will ignore if it is null
     *
     * @param id the id of the payerDTO to save.
     * @param payerDTO the payerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payerDTO,
     * or with status {@code 400 (Bad Request)} if the payerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the payerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the payerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PayerDTO>> partialUpdatePayer(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PayerDTO payerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Payer partially : {}, {}", id, payerDTO);
        if (payerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return payerRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PayerDTO> result = payerService.partialUpdate(payerDTO);

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
     * {@code GET  /payers} : get all the payers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payers in body.
     */
    @GetMapping("/payers")
    public Mono<ResponseEntity<List<PayerDTO>>> getAllPayers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Payers");
        return payerService
            .countAll()
            .zipWith(payerService.findAll(pageable).collectList())
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
     * {@code GET  /payers/:id} : get the "id" payer.
     *
     * @param id the id of the payerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payers/{id}")
    public Mono<ResponseEntity<PayerDTO>> getPayer(@PathVariable String id) {
        log.debug("REST request to get Payer : {}", id);
        Mono<PayerDTO> payerDTO = payerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payerDTO);
    }

    /**
     * {@code DELETE  /payers/:id} : delete the "id" payer.
     *
     * @param id the id of the payerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payers/{id}")
    public Mono<ResponseEntity<Void>> deletePayer(@PathVariable String id) {
        log.debug("REST request to delete Payer : {}", id);
        return payerService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
