package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.ReceiptRepository;
import com.simplifysynergy.service.ReceiptService;
import com.simplifysynergy.service.dto.ReceiptDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.Receipt}.
 */
@RestController
@RequestMapping("/api")
public class ReceiptResource {

    private final Logger log = LoggerFactory.getLogger(ReceiptResource.class);

    private static final String ENTITY_NAME = "collectionsReceipt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceiptService receiptService;

    private final ReceiptRepository receiptRepository;

    public ReceiptResource(ReceiptService receiptService, ReceiptRepository receiptRepository) {
        this.receiptService = receiptService;
        this.receiptRepository = receiptRepository;
    }

    /**
     * {@code POST  /receipts} : Create a new receipt.
     *
     * @param receiptDTO the receiptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receiptDTO, or with status {@code 400 (Bad Request)} if the receipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/receipts")
    public Mono<ResponseEntity<ReceiptDTO>> createReceipt(@Valid @RequestBody ReceiptDTO receiptDTO) throws URISyntaxException {
        log.debug("REST request to save Receipt : {}", receiptDTO);
        if (receiptDTO.getId() != null) {
            throw new BadRequestAlertException("A new receipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return receiptService
            .save(receiptDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/receipts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /receipts/:id} : Updates an existing receipt.
     *
     * @param id the id of the receiptDTO to save.
     * @param receiptDTO the receiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receiptDTO,
     * or with status {@code 400 (Bad Request)} if the receiptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the receiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/receipts/{id}")
    public Mono<ResponseEntity<ReceiptDTO>> updateReceipt(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ReceiptDTO receiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Receipt : {}, {}", id, receiptDTO);
        if (receiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receiptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return receiptRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return receiptService
                    .update(receiptDTO)
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
     * {@code PATCH  /receipts/:id} : Partial updates given fields of an existing receipt, field will ignore if it is null
     *
     * @param id the id of the receiptDTO to save.
     * @param receiptDTO the receiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receiptDTO,
     * or with status {@code 400 (Bad Request)} if the receiptDTO is not valid,
     * or with status {@code 404 (Not Found)} if the receiptDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the receiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/receipts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReceiptDTO>> partialUpdateReceipt(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ReceiptDTO receiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Receipt partially : {}, {}", id, receiptDTO);
        if (receiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receiptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return receiptRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReceiptDTO> result = receiptService.partialUpdate(receiptDTO);

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
     * {@code GET  /receipts} : get all the receipts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of receipts in body.
     */
    @GetMapping("/receipts")
    public Mono<ResponseEntity<List<ReceiptDTO>>> getAllReceipts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Receipts");
        return receiptService
            .countAll()
            .zipWith(receiptService.findAll(pageable).collectList())
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
     * {@code GET  /receipts/:id} : get the "id" receipt.
     *
     * @param id the id of the receiptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receiptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/receipts/{id}")
    public Mono<ResponseEntity<ReceiptDTO>> getReceipt(@PathVariable String id) {
        log.debug("REST request to get Receipt : {}", id);
        Mono<ReceiptDTO> receiptDTO = receiptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(receiptDTO);
    }

    /**
     * {@code DELETE  /receipts/:id} : delete the "id" receipt.
     *
     * @param id the id of the receiptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/receipts/{id}")
    public Mono<ResponseEntity<Void>> deleteReceipt(@PathVariable String id) {
        log.debug("REST request to delete Receipt : {}", id);
        return receiptService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
