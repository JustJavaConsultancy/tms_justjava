package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.CollectionService;
import com.simplifysynergy.repository.CollectionServiceRepository;
import com.simplifysynergy.service.CollectionServiceService;
import com.simplifysynergy.service.dto.CollectionServiceDTO;
import com.simplifysynergy.service.mapper.CollectionServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link CollectionService}.
 */
@Service
public class CollectionServiceServiceImpl implements CollectionServiceService {

    private final Logger log = LoggerFactory.getLogger(CollectionServiceServiceImpl.class);

    private final CollectionServiceRepository collectionServiceRepository;

    private final CollectionServiceMapper collectionServiceMapper;

    public CollectionServiceServiceImpl(
        CollectionServiceRepository collectionServiceRepository,
        CollectionServiceMapper collectionServiceMapper
    ) {
        this.collectionServiceRepository = collectionServiceRepository;
        this.collectionServiceMapper = collectionServiceMapper;
    }

    @Override
    public Mono<CollectionServiceDTO> save(CollectionServiceDTO collectionServiceDTO) {
        log.debug("Request to save CollectionService : {}", collectionServiceDTO);
        return collectionServiceRepository.save(collectionServiceMapper.toEntity(collectionServiceDTO)).map(collectionServiceMapper::toDto);
    }

    @Override
    public Mono<CollectionServiceDTO> update(CollectionServiceDTO collectionServiceDTO) {
        log.debug("Request to update CollectionService : {}", collectionServiceDTO);
        return collectionServiceRepository.save(collectionServiceMapper.toEntity(collectionServiceDTO)).map(collectionServiceMapper::toDto);
    }

    @Override
    public Mono<CollectionServiceDTO> partialUpdate(CollectionServiceDTO collectionServiceDTO) {
        log.debug("Request to partially update CollectionService : {}", collectionServiceDTO);

        return collectionServiceRepository
            .findById(collectionServiceDTO.getId())
            .map(existingCollectionService -> {
                collectionServiceMapper.partialUpdate(existingCollectionService, collectionServiceDTO);

                return existingCollectionService;
            })
            .flatMap(collectionServiceRepository::save)
            .map(collectionServiceMapper::toDto);
    }

    @Override
    public Flux<CollectionServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CollectionServices");
        return collectionServiceRepository.findAllBy(pageable).map(collectionServiceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return collectionServiceRepository.count();
    }

    @Override
    public Mono<CollectionServiceDTO> findOne(String id) {
        log.debug("Request to get CollectionService : {}", id);
        return collectionServiceRepository.findById(id).map(collectionServiceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete CollectionService : {}", id);
        return collectionServiceRepository.deleteById(id);
    }
}
