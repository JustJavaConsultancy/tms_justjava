package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.ServiceType;
import com.simplifysynergy.repository.ServiceTypeRepository;
import com.simplifysynergy.service.ServiceTypeService;
import com.simplifysynergy.service.dto.ServiceTypeDTO;
import com.simplifysynergy.service.mapper.ServiceTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ServiceType}.
 */
@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private final Logger log = LoggerFactory.getLogger(ServiceTypeServiceImpl.class);

    private final ServiceTypeRepository serviceTypeRepository;

    private final ServiceTypeMapper serviceTypeMapper;

    public ServiceTypeServiceImpl(ServiceTypeRepository serviceTypeRepository, ServiceTypeMapper serviceTypeMapper) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceTypeMapper = serviceTypeMapper;
    }

    @Override
    public Mono<ServiceTypeDTO> save(ServiceTypeDTO serviceTypeDTO) {
        log.debug("Request to save ServiceType : {}", serviceTypeDTO);
        return serviceTypeRepository.save(serviceTypeMapper.toEntity(serviceTypeDTO)).map(serviceTypeMapper::toDto);
    }

    @Override
    public Mono<ServiceTypeDTO> update(ServiceTypeDTO serviceTypeDTO) {
        log.debug("Request to update ServiceType : {}", serviceTypeDTO);
        return serviceTypeRepository.save(serviceTypeMapper.toEntity(serviceTypeDTO)).map(serviceTypeMapper::toDto);
    }

    @Override
    public Mono<ServiceTypeDTO> partialUpdate(ServiceTypeDTO serviceTypeDTO) {
        log.debug("Request to partially update ServiceType : {}", serviceTypeDTO);

        return serviceTypeRepository
            .findById(serviceTypeDTO.getId())
            .map(existingServiceType -> {
                serviceTypeMapper.partialUpdate(existingServiceType, serviceTypeDTO);

                return existingServiceType;
            })
            .flatMap(serviceTypeRepository::save)
            .map(serviceTypeMapper::toDto);
    }

    @Override
    public Flux<ServiceTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceTypes");
        return serviceTypeRepository.findAllBy(pageable).map(serviceTypeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return serviceTypeRepository.count();
    }

    @Override
    public Mono<ServiceTypeDTO> findOne(String id) {
        log.debug("Request to get ServiceType : {}", id);
        return serviceTypeRepository.findById(id).map(serviceTypeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ServiceType : {}", id);
        return serviceTypeRepository.deleteById(id);
    }
}
