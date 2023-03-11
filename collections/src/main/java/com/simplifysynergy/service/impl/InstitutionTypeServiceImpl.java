package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.InstitutionType;
import com.simplifysynergy.repository.InstitutionTypeRepository;
import com.simplifysynergy.service.InstitutionTypeService;
import com.simplifysynergy.service.dto.InstitutionTypeDTO;
import com.simplifysynergy.service.mapper.InstitutionTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link InstitutionType}.
 */
@Service
public class InstitutionTypeServiceImpl implements InstitutionTypeService {

    private final Logger log = LoggerFactory.getLogger(InstitutionTypeServiceImpl.class);

    private final InstitutionTypeRepository institutionTypeRepository;

    private final InstitutionTypeMapper institutionTypeMapper;

    public InstitutionTypeServiceImpl(InstitutionTypeRepository institutionTypeRepository, InstitutionTypeMapper institutionTypeMapper) {
        this.institutionTypeRepository = institutionTypeRepository;
        this.institutionTypeMapper = institutionTypeMapper;
    }

    @Override
    public Mono<InstitutionTypeDTO> save(InstitutionTypeDTO institutionTypeDTO) {
        log.debug("Request to save InstitutionType : {}", institutionTypeDTO);
        return institutionTypeRepository.save(institutionTypeMapper.toEntity(institutionTypeDTO)).map(institutionTypeMapper::toDto);
    }

    @Override
    public Mono<InstitutionTypeDTO> update(InstitutionTypeDTO institutionTypeDTO) {
        log.debug("Request to update InstitutionType : {}", institutionTypeDTO);
        return institutionTypeRepository.save(institutionTypeMapper.toEntity(institutionTypeDTO)).map(institutionTypeMapper::toDto);
    }

    @Override
    public Mono<InstitutionTypeDTO> partialUpdate(InstitutionTypeDTO institutionTypeDTO) {
        log.debug("Request to partially update InstitutionType : {}", institutionTypeDTO);

        return institutionTypeRepository
            .findById(institutionTypeDTO.getId())
            .map(existingInstitutionType -> {
                institutionTypeMapper.partialUpdate(existingInstitutionType, institutionTypeDTO);

                return existingInstitutionType;
            })
            .flatMap(institutionTypeRepository::save)
            .map(institutionTypeMapper::toDto);
    }

    @Override
    public Flux<InstitutionTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InstitutionTypes");
        return institutionTypeRepository.findAllBy(pageable).map(institutionTypeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return institutionTypeRepository.count();
    }

    @Override
    public Mono<InstitutionTypeDTO> findOne(String id) {
        log.debug("Request to get InstitutionType : {}", id);
        return institutionTypeRepository.findById(id).map(institutionTypeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete InstitutionType : {}", id);
        return institutionTypeRepository.deleteById(id);
    }
}
