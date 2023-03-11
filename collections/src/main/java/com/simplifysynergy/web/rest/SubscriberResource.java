package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.SubscriberRepository;
import com.simplifysynergy.service.SubscriberService;
import com.simplifysynergy.service.dto.SubscriberDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.Subscriber}.
 */
@RestController
@RequestMapping("/api")
public class SubscriberResource {

    private final Logger log = LoggerFactory.getLogger(SubscriberResource.class);

    private static final String ENTITY_NAME = "collectionsSubscriber";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriberService subscriberService;

    private final SubscriberRepository subscriberRepository;

    public SubscriberResource(SubscriberService subscriberService, SubscriberRepository subscriberRepository) {
        this.subscriberService = subscriberService;
        this.subscriberRepository = subscriberRepository;
    }

    /**
     * {@code POST  /subscribers} : Create a new subscriber.
     *
     * @param subscriberDTO the subscriberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriberDTO, or with status {@code 400 (Bad Request)} if the subscriber has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscribers")
    public Mono<ResponseEntity<SubscriberDTO>> createSubscriber(@RequestBody SubscriberDTO subscriberDTO) throws URISyntaxException {
        log.debug("REST request to save Subscriber : {}", subscriberDTO);
        if (subscriberDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriber cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return subscriberService
            .save(subscriberDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/subscribers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /subscribers/:id} : Updates an existing subscriber.
     *
     * @param id the id of the subscriberDTO to save.
     * @param subscriberDTO the subscriberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriberDTO,
     * or with status {@code 400 (Bad Request)} if the subscriberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscribers/{id}")
    public Mono<ResponseEntity<SubscriberDTO>> updateSubscriber(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SubscriberDTO subscriberDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Subscriber : {}, {}", id, subscriberDTO);
        if (subscriberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subscriberRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return subscriberService
                    .update(subscriberDTO)
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
     * {@code PATCH  /subscribers/:id} : Partial updates given fields of an existing subscriber, field will ignore if it is null
     *
     * @param id the id of the subscriberDTO to save.
     * @param subscriberDTO the subscriberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriberDTO,
     * or with status {@code 400 (Bad Request)} if the subscriberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subscribers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SubscriberDTO>> partialUpdateSubscriber(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SubscriberDTO subscriberDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Subscriber partially : {}, {}", id, subscriberDTO);
        if (subscriberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subscriberRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SubscriberDTO> result = subscriberService.partialUpdate(subscriberDTO);

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
     * {@code GET  /subscribers} : get all the subscribers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscribers in body.
     */
    @GetMapping("/subscribers")
    public Mono<ResponseEntity<List<SubscriberDTO>>> getAllSubscribers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Subscribers");
        return subscriberService
            .countAll()
            .zipWith(subscriberService.findAll(pageable).collectList())
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
     * {@code GET  /subscribers/:id} : get the "id" subscriber.
     *
     * @param id the id of the subscriberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscribers/{id}")
    public Mono<ResponseEntity<SubscriberDTO>> getSubscriber(@PathVariable String id) {
        log.debug("REST request to get Subscriber : {}", id);
        Mono<SubscriberDTO> subscriberDTO = subscriberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriberDTO);
    }

    /**
     * {@code DELETE  /subscribers/:id} : delete the "id" subscriber.
     *
     * @param id the id of the subscriberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscribers/{id}")
    public Mono<ResponseEntity<Void>> deleteSubscriber(@PathVariable String id) {
        log.debug("REST request to delete Subscriber : {}", id);
        return subscriberService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
