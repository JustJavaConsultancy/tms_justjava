package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.Request;
import com.simplifysynergy.repository.RequestRepository;
import com.simplifysynergy.service.RequestService;
import com.simplifysynergy.service.dto.RequestDTO;
import com.simplifysynergy.service.mapper.RequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Request}.
 */
@Service
public class RequestServiceImpl implements RequestService {

    private final Logger log = LoggerFactory.getLogger(RequestServiceImpl.class);

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    public RequestServiceImpl(RequestRepository requestRepository, RequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }

    @Override
    public Mono<RequestDTO> save(RequestDTO requestDTO) {
        log.debug("Request to save Request : {}", requestDTO);
        return requestRepository.save(requestMapper.toEntity(requestDTO)).map(requestMapper::toDto);
    }

    @Override
    public Mono<RequestDTO> update(RequestDTO requestDTO) {
        log.debug("Request to update Request : {}", requestDTO);
        return requestRepository.save(requestMapper.toEntity(requestDTO)).map(requestMapper::toDto);
    }

    @Override
    public Mono<RequestDTO> partialUpdate(RequestDTO requestDTO) {
        log.debug("Request to partially update Request : {}", requestDTO);

        return requestRepository
            .findById(requestDTO.getId())
            .map(existingRequest -> {
                requestMapper.partialUpdate(existingRequest, requestDTO);

                return existingRequest;
            })
            .flatMap(requestRepository::save)
            .map(requestMapper::toDto);
    }

    @Override
    public Flux<RequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Requests");
        return requestRepository.findAllBy(pageable).map(requestMapper::toDto);
    }

    public Mono<Long> countAll() {
        return requestRepository.count();
    }

    @Override
    public Mono<RequestDTO> findOne(String id) {
        log.debug("Request to get Request : {}", id);
        return requestRepository.findById(id).map(requestMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Request : {}", id);
        return requestRepository.deleteById(id);
    }
}
