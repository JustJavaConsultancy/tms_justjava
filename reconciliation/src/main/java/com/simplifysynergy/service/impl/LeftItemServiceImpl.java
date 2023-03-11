package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.LeftItem;
import com.simplifysynergy.repository.LeftItemRepository;
import com.simplifysynergy.service.LeftItemService;
import com.simplifysynergy.service.dto.LeftItemDTO;
import com.simplifysynergy.service.mapper.LeftItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link LeftItem}.
 */
@Service
public class LeftItemServiceImpl implements LeftItemService {

    private final Logger log = LoggerFactory.getLogger(LeftItemServiceImpl.class);

    private final LeftItemRepository leftItemRepository;

    private final LeftItemMapper leftItemMapper;

    public LeftItemServiceImpl(LeftItemRepository leftItemRepository, LeftItemMapper leftItemMapper) {
        this.leftItemRepository = leftItemRepository;
        this.leftItemMapper = leftItemMapper;
    }

    @Override
    public Mono<LeftItemDTO> save(LeftItemDTO leftItemDTO) {
        log.debug("Request to save LeftItem : {}", leftItemDTO);
        return leftItemRepository.save(leftItemMapper.toEntity(leftItemDTO)).map(leftItemMapper::toDto);
    }

    @Override
    public Mono<LeftItemDTO> update(LeftItemDTO leftItemDTO) {
        log.debug("Request to update LeftItem : {}", leftItemDTO);
        return leftItemRepository.save(leftItemMapper.toEntity(leftItemDTO)).map(leftItemMapper::toDto);
    }

    @Override
    public Mono<LeftItemDTO> partialUpdate(LeftItemDTO leftItemDTO) {
        log.debug("Request to partially update LeftItem : {}", leftItemDTO);

        return leftItemRepository
            .findById(leftItemDTO.getId())
            .map(existingLeftItem -> {
                leftItemMapper.partialUpdate(existingLeftItem, leftItemDTO);

                return existingLeftItem;
            })
            .flatMap(leftItemRepository::save)
            .map(leftItemMapper::toDto);
    }

    @Override
    public Flux<LeftItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeftItems");
        return leftItemRepository.findAllBy(pageable).map(leftItemMapper::toDto);
    }

    public Mono<Long> countAll() {
        return leftItemRepository.count();
    }

    @Override
    public Mono<LeftItemDTO> findOne(String id) {
        log.debug("Request to get LeftItem : {}", id);
        return leftItemRepository.findById(id).map(leftItemMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete LeftItem : {}", id);
        return leftItemRepository.deleteById(id);
    }
}
