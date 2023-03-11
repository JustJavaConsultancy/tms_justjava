package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.ReconciliationItem;
import com.simplifysynergy.repository.ReconciliationItemRepository;
import com.simplifysynergy.service.ReconciliationItemService;
import com.simplifysynergy.service.dto.ReconciliationItemDTO;
import com.simplifysynergy.service.mapper.ReconciliationItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ReconciliationItem}.
 */
@Service
public class ReconciliationItemServiceImpl implements ReconciliationItemService {

    private final Logger log = LoggerFactory.getLogger(ReconciliationItemServiceImpl.class);

    private final ReconciliationItemRepository reconciliationItemRepository;

    private final ReconciliationItemMapper reconciliationItemMapper;

    public ReconciliationItemServiceImpl(
        ReconciliationItemRepository reconciliationItemRepository,
        ReconciliationItemMapper reconciliationItemMapper
    ) {
        this.reconciliationItemRepository = reconciliationItemRepository;
        this.reconciliationItemMapper = reconciliationItemMapper;
    }

    @Override
    public Mono<ReconciliationItemDTO> save(ReconciliationItemDTO reconciliationItemDTO) {
        log.debug("Request to save ReconciliationItem : {}", reconciliationItemDTO);
        return reconciliationItemRepository
            .save(reconciliationItemMapper.toEntity(reconciliationItemDTO))
            .map(reconciliationItemMapper::toDto);
    }

    @Override
    public Mono<ReconciliationItemDTO> update(ReconciliationItemDTO reconciliationItemDTO) {
        log.debug("Request to update ReconciliationItem : {}", reconciliationItemDTO);
        return reconciliationItemRepository
            .save(reconciliationItemMapper.toEntity(reconciliationItemDTO))
            .map(reconciliationItemMapper::toDto);
    }

    @Override
    public Mono<ReconciliationItemDTO> partialUpdate(ReconciliationItemDTO reconciliationItemDTO) {
        log.debug("Request to partially update ReconciliationItem : {}", reconciliationItemDTO);

        return reconciliationItemRepository
            .findById(reconciliationItemDTO.getId())
            .map(existingReconciliationItem -> {
                reconciliationItemMapper.partialUpdate(existingReconciliationItem, reconciliationItemDTO);

                return existingReconciliationItem;
            })
            .flatMap(reconciliationItemRepository::save)
            .map(reconciliationItemMapper::toDto);
    }

    @Override
    public Flux<ReconciliationItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReconciliationItems");
        return reconciliationItemRepository.findAllBy(pageable).map(reconciliationItemMapper::toDto);
    }

    public Mono<Long> countAll() {
        return reconciliationItemRepository.count();
    }

    @Override
    public Mono<ReconciliationItemDTO> findOne(String id) {
        log.debug("Request to get ReconciliationItem : {}", id);
        return reconciliationItemRepository.findById(id).map(reconciliationItemMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ReconciliationItem : {}", id);
        return reconciliationItemRepository.deleteById(id);
    }
}
