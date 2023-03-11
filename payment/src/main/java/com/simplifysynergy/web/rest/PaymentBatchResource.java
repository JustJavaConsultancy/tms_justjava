package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.PaymentBatchRepository;
import com.simplifysynergy.service.PaymentBatchService;
import com.simplifysynergy.service.dto.PaymentBatchDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.PaymentBatch}.
 */
@RestController
@RequestMapping("/api")
public class PaymentBatchResource {

    private final Logger log = LoggerFactory.getLogger(PaymentBatchResource.class);

    private static final String ENTITY_NAME = "paymentPaymentBatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentBatchService paymentBatchService;

    private final PaymentBatchRepository paymentBatchRepository;

    public PaymentBatchResource(PaymentBatchService paymentBatchService, PaymentBatchRepository paymentBatchRepository) {
        this.paymentBatchService = paymentBatchService;
        this.paymentBatchRepository = paymentBatchRepository;
    }

    /**
     * {@code POST  /payment-batches} : Create a new paymentBatch.
     *
     * @param paymentBatchDTO the paymentBatchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentBatchDTO, or with status {@code 400 (Bad Request)} if the paymentBatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-batches")
    public Mono<ResponseEntity<PaymentBatchDTO>> createPaymentBatch(@Valid @RequestBody PaymentBatchDTO paymentBatchDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaymentBatch : {}", paymentBatchDTO);
        if (paymentBatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentBatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return paymentBatchService
            .save(paymentBatchDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/payment-batches/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /payment-batches/:id} : Updates an existing paymentBatch.
     *
     * @param id the id of the paymentBatchDTO to save.
     * @param paymentBatchDTO the paymentBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentBatchDTO,
     * or with status {@code 400 (Bad Request)} if the paymentBatchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-batches/{id}")
    public Mono<ResponseEntity<PaymentBatchDTO>> updatePaymentBatch(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody PaymentBatchDTO paymentBatchDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentBatch : {}, {}", id, paymentBatchDTO);
        if (paymentBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentBatchRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return paymentBatchService
                    .update(paymentBatchDTO)
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
     * {@code PATCH  /payment-batches/:id} : Partial updates given fields of an existing paymentBatch, field will ignore if it is null
     *
     * @param id the id of the paymentBatchDTO to save.
     * @param paymentBatchDTO the paymentBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentBatchDTO,
     * or with status {@code 400 (Bad Request)} if the paymentBatchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentBatchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-batches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PaymentBatchDTO>> partialUpdatePaymentBatch(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PaymentBatchDTO paymentBatchDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentBatch partially : {}, {}", id, paymentBatchDTO);
        if (paymentBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentBatchRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PaymentBatchDTO> result = paymentBatchService.partialUpdate(paymentBatchDTO);

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
     * {@code GET  /payment-batches} : get all the paymentBatches.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentBatches in body.
     */
    @GetMapping("/payment-batches")
    public Mono<ResponseEntity<List<PaymentBatchDTO>>> getAllPaymentBatches(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of PaymentBatches");
        return paymentBatchService
            .countAll()
            .zipWith(paymentBatchService.findAll(pageable).collectList())
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
     * {@code GET  /payment-batches/:id} : get the "id" paymentBatch.
     *
     * @param id the id of the paymentBatchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentBatchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-batches/{id}")
    public Mono<ResponseEntity<PaymentBatchDTO>> getPaymentBatch(@PathVariable String id) {
        log.debug("REST request to get PaymentBatch : {}", id);
        Mono<PaymentBatchDTO> paymentBatchDTO = paymentBatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentBatchDTO);
    }

    /**
     * {@code DELETE  /payment-batches/:id} : delete the "id" paymentBatch.
     *
     * @param id the id of the paymentBatchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-batches/{id}")
    public Mono<ResponseEntity<Void>> deletePaymentBatch(@PathVariable String id) {
        log.debug("REST request to delete PaymentBatch : {}", id);
        return paymentBatchService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
