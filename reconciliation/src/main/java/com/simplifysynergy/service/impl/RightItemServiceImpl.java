package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.RightItem;
import com.simplifysynergy.repository.RightItemRepository;
import com.simplifysynergy.service.RightItemService;
import com.simplifysynergy.service.dto.RightItemDTO;
import com.simplifysynergy.service.mapper.RightItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link RightItem}.
 */
@Service
public class RightItemServiceImpl implements RightItemService {

    private final Logger log = LoggerFactory.getLogger(RightItemServiceImpl.class);

    private final RightItemRepository rightItemRepository;

    private final RightItemMapper rightItemMapper;

    public RightItemServiceImpl(RightItemRepository rightItemRepository, RightItemMapper rightItemMapper) {
        this.rightItemRepository = rightItemRepository;
        this.rightItemMapper = rightItemMapper;
    }

    @Override
    public Mono<RightItemDTO> save(RightItemDTO rightItemDTO) {
        log.debug("Request to save RightItem : {}", rightItemDTO);
        return rightItemRepository.save(rightItemMapper.toEntity(rightItemDTO)).map(rightItemMapper::toDto);
    }

    @Override
    public Mono<RightItemDTO> update(RightItemDTO rightItemDTO) {
        log.debug("Request to update RightItem : {}", rightItemDTO);
        return rightItemRepository.save(rightItemMapper.toEntity(rightItemDTO)).map(rightItemMapper::toDto);
    }

    @Override
    public Mono<RightItemDTO> partialUpdate(RightItemDTO rightItemDTO) {
        log.debug("Request to partially update RightItem : {}", rightItemDTO);

        return rightItemRepository
            .findById(rightItemDTO.getId())
            .map(existingRightItem -> {
                rightItemMapper.partialUpdate(existingRightItem, rightItemDTO);

                return existingRightItem;
            })
            .flatMap(rightItemRepository::save)
            .map(rightItemMapper::toDto);
    }

    @Override
    public Flux<RightItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RightItems");
        return rightItemRepository.findAllBy(pageable).map(rightItemMapper::toDto);
    }

    public Mono<Long> countAll() {
        return rightItemRepository.count();
    }

    @Override
    public Mono<RightItemDTO> findOne(String id) {
        log.debug("Request to get RightItem : {}", id);
        return rightItemRepository.findById(id).map(rightItemMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete RightItem : {}", id);
        return rightItemRepository.deleteById(id);
    }
}
