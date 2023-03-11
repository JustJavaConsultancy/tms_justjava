package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.Institution;
import com.simplifysynergy.repository.InstitutionRepository;
import com.simplifysynergy.service.InstitutionService;
import com.simplifysynergy.service.dto.InstitutionDTO;
import com.simplifysynergy.service.mapper.InstitutionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Institution}.
 */
@Service
public class InstitutionServiceImpl implements InstitutionService {

    private final Logger log = LoggerFactory.getLogger(InstitutionServiceImpl.class);

    private final InstitutionRepository institutionRepository;

    private final InstitutionMapper institutionMapper;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository, InstitutionMapper institutionMapper) {
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
    }

    @Override
    public Mono<InstitutionDTO> save(InstitutionDTO institutionDTO) {
        log.debug("Request to save Institution : {}", institutionDTO);
        return institutionRepository.save(institutionMapper.toEntity(institutionDTO)).map(institutionMapper::toDto);
    }

    @Override
    public Mono<InstitutionDTO> update(InstitutionDTO institutionDTO) {
        log.debug("Request to update Institution : {}", institutionDTO);
        return institutionRepository.save(institutionMapper.toEntity(institutionDTO)).map(institutionMapper::toDto);
    }

    @Override
    public Mono<InstitutionDTO> partialUpdate(InstitutionDTO institutionDTO) {
        log.debug("Request to partially update Institution : {}", institutionDTO);

        return institutionRepository
            .findById(institutionDTO.getId())
            .map(existingInstitution -> {
                institutionMapper.partialUpdate(existingInstitution, institutionDTO);

                return existingInstitution;
            })
            .flatMap(institutionRepository::save)
            .map(institutionMapper::toDto);
    }

    @Override
    public Flux<InstitutionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Institutions");
        return institutionRepository.findAllBy(pageable).map(institutionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return institutionRepository.count();
    }

    @Override
    public Mono<InstitutionDTO> findOne(String id) {
        log.debug("Request to get Institution : {}", id);
        return institutionRepository.findById(id).map(institutionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Institution : {}", id);
        return institutionRepository.deleteById(id);
    }
}
