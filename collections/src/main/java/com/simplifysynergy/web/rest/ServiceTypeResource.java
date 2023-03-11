package com.simplifysynergy.web.rest;

import com.simplifysynergy.repository.ServiceTypeRepository;
import com.simplifysynergy.service.ServiceTypeService;
import com.simplifysynergy.service.dto.ServiceTypeDTO;
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
 * REST controller for managing {@link com.simplifysynergy.domain.ServiceType}.
 */
@RestController
@RequestMapping("/api")
public class ServiceTypeResource {

    private final Logger log = LoggerFactory.getLogger(ServiceTypeResource.class);

    private static final String ENTITY_NAME = "collectionsServiceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceTypeService serviceTypeService;

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeResource(ServiceTypeService serviceTypeService, ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeService = serviceTypeService;
        this.serviceTypeRepository = serviceTypeRepository;
    }

    /**
     * {@code POST  /service-types} : Create a new serviceType.
     *
     * @param serviceTypeDTO the serviceTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceTypeDTO, or with status {@code 400 (Bad Request)} if the serviceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-types")
    public Mono<ResponseEntity<ServiceTypeDTO>> createServiceType(@RequestBody ServiceTypeDTO serviceTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceType : {}", serviceTypeDTO);
        if (serviceTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return serviceTypeService
            .save(serviceTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/service-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /service-types/:id} : Updates an existing serviceType.
     *
     * @param id the id of the serviceTypeDTO to save.
     * @param serviceTypeDTO the serviceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the serviceTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-types/{id}")
    public Mono<ResponseEntity<ServiceTypeDTO>> updateServiceType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ServiceTypeDTO serviceTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceType : {}, {}", id, serviceTypeDTO);
        if (serviceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return serviceTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return serviceTypeService
                    .update(serviceTypeDTO)
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
     * {@code PATCH  /service-types/:id} : Partial updates given fields of an existing serviceType, field will ignore if it is null
     *
     * @param id the id of the serviceTypeDTO to save.
     * @param serviceTypeDTO the serviceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the serviceTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ServiceTypeDTO>> partialUpdateServiceType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ServiceTypeDTO serviceTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceType partially : {}, {}", id, serviceTypeDTO);
        if (serviceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return serviceTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ServiceTypeDTO> result = serviceTypeService.partialUpdate(serviceTypeDTO);

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
     * {@code GET  /service-types} : get all the serviceTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceTypes in body.
     */
    @GetMapping("/service-types")
    public Mono<ResponseEntity<List<ServiceTypeDTO>>> getAllServiceTypes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ServiceTypes");
        return serviceTypeService
            .countAll()
            .zipWith(serviceTypeService.findAll(pageable).collectList())
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
     * {@code GET  /service-types/:id} : get the "id" serviceType.
     *
     * @param id the id of the serviceTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-types/{id}")
    public Mono<ResponseEntity<ServiceTypeDTO>> getServiceType(@PathVariable String id) {
        log.debug("REST request to get ServiceType : {}", id);
        Mono<ServiceTypeDTO> serviceTypeDTO = serviceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceTypeDTO);
    }

    /**
     * {@code DELETE  /service-types/:id} : delete the "id" serviceType.
     *
     * @param id the id of the serviceTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-types/{id}")
    public Mono<ResponseEntity<Void>> deleteServiceType(@PathVariable String id) {
        log.debug("REST request to delete ServiceType : {}", id);
        return serviceTypeService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
