package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.RequestType;
import com.simplifysynergy.repository.RequestTypeRepository;
import com.simplifysynergy.service.RequestTypeService;
import com.simplifysynergy.service.dto.RequestTypeDTO;
import com.simplifysynergy.service.mapper.RequestTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link RequestType}.
 */
@Service
public class RequestTypeServiceImpl implements RequestTypeService {

    private final Logger log = LoggerFactory.getLogger(RequestTypeServiceImpl.class);

    private final RequestTypeRepository requestTypeRepository;

    private final RequestTypeMapper requestTypeMapper;

    public RequestTypeServiceImpl(RequestTypeRepository requestTypeRepository, RequestTypeMapper requestTypeMapper) {
        this.requestTypeRepository = requestTypeRepository;
        this.requestTypeMapper = requestTypeMapper;
    }

    @Override
    public Mono<RequestTypeDTO> save(RequestTypeDTO requestTypeDTO) {
        log.debug("Request to save RequestType : {}", requestTypeDTO);
        return requestTypeRepository.save(requestTypeMapper.toEntity(requestTypeDTO)).map(requestTypeMapper::toDto);
    }

    @Override
    public Mono<RequestTypeDTO> update(RequestTypeDTO requestTypeDTO) {
        log.debug("Request to update RequestType : {}", requestTypeDTO);
        return requestTypeRepository.save(requestTypeMapper.toEntity(requestTypeDTO)).map(requestTypeMapper::toDto);
    }

    @Override
    public Mono<RequestTypeDTO> partialUpdate(RequestTypeDTO requestTypeDTO) {
        log.debug("Request to partially update RequestType : {}", requestTypeDTO);

        return requestTypeRepository
            .findById(requestTypeDTO.getId())
            .map(existingRequestType -> {
                requestTypeMapper.partialUpdate(existingRequestType, requestTypeDTO);

                return existingRequestType;
            })
            .flatMap(requestTypeRepository::save)
            .map(requestTypeMapper::toDto);
    }

    @Override
    public Flux<RequestTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RequestTypes");
        return requestTypeRepository.findAllBy(pageable).map(requestTypeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return requestTypeRepository.count();
    }

    @Override
    public Mono<RequestTypeDTO> findOne(String id) {
        log.debug("Request to get RequestType : {}", id);
        return requestTypeRepository.findById(id).map(requestTypeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete RequestType : {}", id);
        return requestTypeRepository.deleteById(id);
    }
}
