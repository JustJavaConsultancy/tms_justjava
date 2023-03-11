package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.ApprovalRoute;
import com.simplifysynergy.repository.ApprovalRouteRepository;
import com.simplifysynergy.service.ApprovalRouteService;
import com.simplifysynergy.service.dto.ApprovalRouteDTO;
import com.simplifysynergy.service.mapper.ApprovalRouteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ApprovalRoute}.
 */
@Service
public class ApprovalRouteServiceImpl implements ApprovalRouteService {

    private final Logger log = LoggerFactory.getLogger(ApprovalRouteServiceImpl.class);

    private final ApprovalRouteRepository approvalRouteRepository;

    private final ApprovalRouteMapper approvalRouteMapper;

    public ApprovalRouteServiceImpl(ApprovalRouteRepository approvalRouteRepository, ApprovalRouteMapper approvalRouteMapper) {
        this.approvalRouteRepository = approvalRouteRepository;
        this.approvalRouteMapper = approvalRouteMapper;
    }

    @Override
    public Mono<ApprovalRouteDTO> save(ApprovalRouteDTO approvalRouteDTO) {
        log.debug("Request to save ApprovalRoute : {}", approvalRouteDTO);
        return approvalRouteRepository.save(approvalRouteMapper.toEntity(approvalRouteDTO)).map(approvalRouteMapper::toDto);
    }

    @Override
    public Mono<ApprovalRouteDTO> update(ApprovalRouteDTO approvalRouteDTO) {
        log.debug("Request to update ApprovalRoute : {}", approvalRouteDTO);
        return approvalRouteRepository.save(approvalRouteMapper.toEntity(approvalRouteDTO)).map(approvalRouteMapper::toDto);
    }

    @Override
    public Mono<ApprovalRouteDTO> partialUpdate(ApprovalRouteDTO approvalRouteDTO) {
        log.debug("Request to partially update ApprovalRoute : {}", approvalRouteDTO);

        return approvalRouteRepository
            .findById(approvalRouteDTO.getId())
            .map(existingApprovalRoute -> {
                approvalRouteMapper.partialUpdate(existingApprovalRoute, approvalRouteDTO);

                return existingApprovalRoute;
            })
            .flatMap(approvalRouteRepository::save)
            .map(approvalRouteMapper::toDto);
    }

    @Override
    public Flux<ApprovalRouteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApprovalRoutes");
        return approvalRouteRepository.findAllBy(pageable).map(approvalRouteMapper::toDto);
    }

    public Flux<ApprovalRouteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return approvalRouteRepository.findAllWithEagerRelationships(pageable).map(approvalRouteMapper::toDto);
    }

    public Mono<Long> countAll() {
        return approvalRouteRepository.count();
    }

    @Override
    public Mono<ApprovalRouteDTO> findOne(String id) {
        log.debug("Request to get ApprovalRoute : {}", id);
        return approvalRouteRepository.findOneWithEagerRelationships(id).map(approvalRouteMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ApprovalRoute : {}", id);
        return approvalRouteRepository.deleteById(id);
    }
}
