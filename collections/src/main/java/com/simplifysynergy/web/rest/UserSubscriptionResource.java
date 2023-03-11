package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.UserSubscriptionRepository;
import com.simplifysynergy.service.UserSubscriptionService;
import com.simplifysynergy.service.dto.UserSubscriptionDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.UserSubscription}.
 */
@RestController
@RequestMapping("/api")
public class UserSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(UserSubscriptionResource.class);

    private static final String ENTITY_NAME = "collectionsUserSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSubscriptionService userSubscriptionService;

    private final UserSubscriptionRepository userSubscriptionRepository;

    public UserSubscriptionResource(
        UserSubscriptionService userSubscriptionService,
        UserSubscriptionRepository userSubscriptionRepository
    ) {
        this.userSubscriptionService = userSubscriptionService;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    /**
     * {@code POST  /user-subscriptions} : Create a new userSubscription.
     *
     * @param userSubscriptionDTO the userSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSubscriptionDTO, or with status {@code 400 (Bad Request)} if the userSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-subscriptions")
    public Mono<ResponseEntity<UserSubscriptionDTO>> createUserSubscription(@RequestBody UserSubscriptionDTO userSubscriptionDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserSubscription : {}", userSubscriptionDTO);
        if (userSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userSubscriptionService
            .save(userSubscriptionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-subscriptions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-subscriptions/:id} : Updates an existing userSubscription.
     *
     * @param id the id of the userSubscriptionDTO to save.
     * @param userSubscriptionDTO the userSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the userSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-subscriptions/{id}")
    public Mono<ResponseEntity<UserSubscriptionDTO>> updateUserSubscription(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody UserSubscriptionDTO userSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserSubscription : {}, {}", id, userSubscriptionDTO);
        if (userSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userSubscriptionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userSubscriptionService
                    .update(userSubscriptionDTO)
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
     * {@code PATCH  /user-subscriptions/:id} : Partial updates given fields of an existing userSubscription, field will ignore if it is null
     *
     * @param id the id of the userSubscriptionDTO to save.
     * @param userSubscriptionDTO the userSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the userSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-subscriptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserSubscriptionDTO>> partialUpdateUserSubscription(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody UserSubscriptionDTO userSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserSubscription partially : {}, {}", id, userSubscriptionDTO);
        if (userSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userSubscriptionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserSubscriptionDTO> result = userSubscriptionService.partialUpdate(userSubscriptionDTO);

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
     * {@code GET  /user-subscriptions} : get all the userSubscriptions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSubscriptions in body.
     */
    @GetMapping("/user-subscriptions")
    public Mono<ResponseEntity<List<UserSubscriptionDTO>>> getAllUserSubscriptions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserSubscriptions");
        return userSubscriptionService
            .countAll()
            .zipWith(userSubscriptionService.findAll(pageable).collectList())
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
     * {@code GET  /user-subscriptions/:id} : get the "id" userSubscription.
     *
     * @param id the id of the userSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-subscriptions/{id}")
    public Mono<ResponseEntity<UserSubscriptionDTO>> getUserSubscription(@PathVariable String id) {
        log.debug("REST request to get UserSubscription : {}", id);
        Mono<UserSubscriptionDTO> userSubscriptionDTO = userSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSubscriptionDTO);
    }

    /**
     * {@code DELETE  /user-subscriptions/:id} : delete the "id" userSubscription.
     *
     * @param id the id of the userSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-subscriptions/{id}")
    public Mono<ResponseEntity<Void>> deleteUserSubscription(@PathVariable String id) {
        log.debug("REST request to delete UserSubscription : {}", id);
        return userSubscriptionService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
