package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.InvoicePaymentRepository;
import com.simplifysynergy.service.InvoicePaymentService;
import com.simplifysynergy.service.dto.InvoicePaymentDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.InvoicePayment}.
 */
@RestController
@RequestMapping("/api")
public class InvoicePaymentResource {

    private final Logger log = LoggerFactory.getLogger(InvoicePaymentResource.class);

    private static final String ENTITY_NAME = "collectionsInvoicePayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoicePaymentService invoicePaymentService;

    private final InvoicePaymentRepository invoicePaymentRepository;

    public InvoicePaymentResource(InvoicePaymentService invoicePaymentService, InvoicePaymentRepository invoicePaymentRepository) {
        this.invoicePaymentService = invoicePaymentService;
        this.invoicePaymentRepository = invoicePaymentRepository;
    }

    /**
     * {@code POST  /invoice-payments} : Create a new invoicePayment.
     *
     * @param invoicePaymentDTO the invoicePaymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoicePaymentDTO, or with status {@code 400 (Bad Request)} if the invoicePayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-payments")
    public Mono<ResponseEntity<InvoicePaymentDTO>> createInvoicePayment(@RequestBody InvoicePaymentDTO invoicePaymentDTO)
        throws URISyntaxException {
        log.debug("REST request to save InvoicePayment : {}", invoicePaymentDTO);
        if (invoicePaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoicePayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return invoicePaymentService
            .save(invoicePaymentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/invoice-payments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /invoice-payments/:id} : Updates an existing invoicePayment.
     *
     * @param id the id of the invoicePaymentDTO to save.
     * @param invoicePaymentDTO the invoicePaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoicePaymentDTO,
     * or with status {@code 400 (Bad Request)} if the invoicePaymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoicePaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-payments/{id}")
    public Mono<ResponseEntity<InvoicePaymentDTO>> updateInvoicePayment(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InvoicePaymentDTO invoicePaymentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InvoicePayment : {}, {}", id, invoicePaymentDTO);
        if (invoicePaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoicePaymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return invoicePaymentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return invoicePaymentService
                    .update(invoicePaymentDTO)
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
     * {@code PATCH  /invoice-payments/:id} : Partial updates given fields of an existing invoicePayment, field will ignore if it is null
     *
     * @param id the id of the invoicePaymentDTO to save.
     * @param invoicePaymentDTO the invoicePaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoicePaymentDTO,
     * or with status {@code 400 (Bad Request)} if the invoicePaymentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoicePaymentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoicePaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/invoice-payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InvoicePaymentDTO>> partialUpdateInvoicePayment(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InvoicePaymentDTO invoicePaymentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoicePayment partially : {}, {}", id, invoicePaymentDTO);
        if (invoicePaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoicePaymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return invoicePaymentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InvoicePaymentDTO> result = invoicePaymentService.partialUpdate(invoicePaymentDTO);

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
     * {@code GET  /invoice-payments} : get all the invoicePayments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoicePayments in body.
     */
    @GetMapping("/invoice-payments")
    public Mono<ResponseEntity<List<InvoicePaymentDTO>>> getAllInvoicePayments(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of InvoicePayments");
        return invoicePaymentService
            .countAll()
            .zipWith(invoicePaymentService.findAll(pageable).collectList())
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
     * {@code GET  /invoice-payments/:id} : get the "id" invoicePayment.
     *
     * @param id the id of the invoicePaymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoicePaymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-payments/{id}")
    public Mono<ResponseEntity<InvoicePaymentDTO>> getInvoicePayment(@PathVariable String id) {
        log.debug("REST request to get InvoicePayment : {}", id);
        Mono<InvoicePaymentDTO> invoicePaymentDTO = invoicePaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoicePaymentDTO);
    }

    /**
     * {@code DELETE  /invoice-payments/:id} : delete the "id" invoicePayment.
     *
     * @param id the id of the invoicePaymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-payments/{id}")
    public Mono<ResponseEntity<Void>> deleteInvoicePayment(@PathVariable String id) {
        log.debug("REST request to delete InvoicePayment : {}", id);
        return invoicePaymentService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
