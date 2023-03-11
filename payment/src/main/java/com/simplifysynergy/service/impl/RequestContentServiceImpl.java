package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.RequestContent;
import com.simplifysynergy.repository.RequestContentRepository;
import com.simplifysynergy.service.RequestContentService;
import com.simplifysynergy.service.dto.RequestContentDTO;
import com.simplifysynergy.service.mapper.RequestContentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link RequestContent}.
 */
@Service
public class RequestContentServiceImpl implements RequestContentService {

    private final Logger log = LoggerFactory.getLogger(RequestContentServiceImpl.class);

    private final RequestContentRepository requestContentRepository;

    private final RequestContentMapper requestContentMapper;

    public RequestContentServiceImpl(RequestContentRepository requestContentRepository, RequestContentMapper requestContentMapper) {
        this.requestContentRepository = requestContentRepository;
        this.requestContentMapper = requestContentMapper;
    }

    @Override
    public Mono<RequestContentDTO> save(RequestContentDTO requestContentDTO) {
        log.debug("Request to save RequestContent : {}", requestContentDTO);
        return requestContentRepository.save(requestContentMapper.toEntity(requestContentDTO)).map(requestContentMapper::toDto);
    }

    @Override
    public Mono<RequestContentDTO> update(RequestContentDTO requestContentDTO) {
        log.debug("Request to update RequestContent : {}", requestContentDTO);
        return requestContentRepository.save(requestContentMapper.toEntity(requestContentDTO)).map(requestContentMapper::toDto);
    }

    @Override
    public Mono<RequestContentDTO> partialUpdate(RequestContentDTO requestContentDTO) {
        log.debug("Request to partially update RequestContent : {}", requestContentDTO);

        return requestContentRepository
            .findById(requestContentDTO.getId())
            .map(existingRequestContent -> {
                requestContentMapper.partialUpdate(existingRequestContent, requestContentDTO);

                return existingRequestContent;
            })
            .flatMap(requestContentRepository::save)
            .map(requestContentMapper::toDto);
    }

    @Override
    public Flux<RequestContentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RequestContents");
        return requestContentRepository.findAllBy(pageable).map(requestContentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return requestContentRepository.count();
    }

    @Override
    public Mono<RequestContentDTO> findOne(String id) {
        log.debug("Request to get RequestContent : {}", id);
        return requestContentRepository.findById(id).map(requestContentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete RequestContent : {}", id);
        return requestContentRepository.deleteById(id);
    }
}
