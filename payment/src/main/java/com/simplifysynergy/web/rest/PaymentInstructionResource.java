package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.PaymentInstructionRepository;
import com.simplifysynergy.service.PaymentInstructionService;
import com.simplifysynergy.service.dto.PaymentInstructionDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.PaymentInstruction}.
 */
@RestController
@RequestMapping("/api")
public class PaymentInstructionResource {

    private final Logger log = LoggerFactory.getLogger(PaymentInstructionResource.class);

    private static final String ENTITY_NAME = "paymentPaymentInstruction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentInstructionService paymentInstructionService;

    private final PaymentInstructionRepository paymentInstructionRepository;

    public PaymentInstructionResource(
        PaymentInstructionService paymentInstructionService,
        PaymentInstructionRepository paymentInstructionRepository
    ) {
        this.paymentInstructionService = paymentInstructionService;
        this.paymentInstructionRepository = paymentInstructionRepository;
    }

    /**
     * {@code POST  /payment-instructions} : Create a new paymentInstruction.
     *
     * @param paymentInstructionDTO the paymentInstructionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentInstructionDTO, or with status {@code 400 (Bad Request)} if the paymentInstruction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-instructions")
    public Mono<ResponseEntity<PaymentInstructionDTO>> createPaymentInstruction(
        @Valid @RequestBody PaymentInstructionDTO paymentInstructionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PaymentInstruction : {}", paymentInstructionDTO);
        if (paymentInstructionDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentInstruction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return paymentInstructionService
            .save(paymentInstructionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/payment-instructions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /payment-instructions/:id} : Updates an existing paymentInstruction.
     *
     * @param id the id of the paymentInstructionDTO to save.
     * @param paymentInstructionDTO the paymentInstructionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentInstructionDTO,
     * or with status {@code 400 (Bad Request)} if the paymentInstructionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentInstructionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-instructions/{id}")
    public Mono<ResponseEntity<PaymentInstructionDTO>> updatePaymentInstruction(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody PaymentInstructionDTO paymentInstructionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentInstruction : {}, {}", id, paymentInstructionDTO);
        if (paymentInstructionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentInstructionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentInstructionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return paymentInstructionService
                    .update(paymentInstructionDTO)
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
     * {@code PATCH  /payment-instructions/:id} : Partial updates given fields of an existing paymentInstruction, field will ignore if it is null
     *
     * @param id the id of the paymentInstructionDTO to save.
     * @param paymentInstructionDTO the paymentInstructionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentInstructionDTO,
     * or with status {@code 400 (Bad Request)} if the paymentInstructionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentInstructionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentInstructionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-instructions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PaymentInstructionDTO>> partialUpdatePaymentInstruction(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PaymentInstructionDTO paymentInstructionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentInstruction partially : {}, {}", id, paymentInstructionDTO);
        if (paymentInstructionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentInstructionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentInstructionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PaymentInstructionDTO> result = paymentInstructionService.partialUpdate(paymentInstructionDTO);

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
     * {@code GET  /payment-instructions} : get all the paymentInstructions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentInstructions in body.
     */
    @GetMapping("/payment-instructions")
    public Mono<ResponseEntity<List<PaymentInstructionDTO>>> getAllPaymentInstructions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of PaymentInstructions");
        return paymentInstructionService
            .countAll()
            .zipWith(paymentInstructionService.findAll(pageable).collectList())
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
     * {@code GET  /payment-instructions/:id} : get the "id" paymentInstruction.
     *
     * @param id the id of the paymentInstructionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentInstructionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-instructions/{id}")
    public Mono<ResponseEntity<PaymentInstructionDTO>> getPaymentInstruction(@PathVariable String id) {
        log.debug("REST request to get PaymentInstruction : {}", id);
        Mono<PaymentInstructionDTO> paymentInstructionDTO = paymentInstructionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentInstructionDTO);
    }

    /**
     * {@code DELETE  /payment-instructions/:id} : delete the "id" paymentInstruction.
     *
     * @param id the id of the paymentInstructionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-instructions/{id}")
    public Mono<ResponseEntity<Void>> deletePaymentInstruction(@PathVariable String id) {
        log.debug("REST request to delete PaymentInstruction : {}", id);
        return paymentInstructionService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
