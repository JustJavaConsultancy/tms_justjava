package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.ApprovalLine;
import com.simplifysynergy.repository.ApprovalLineRepository;
import com.simplifysynergy.service.ApprovalLineService;
import com.simplifysynergy.service.dto.ApprovalLineDTO;
import com.simplifysynergy.service.mapper.ApprovalLineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ApprovalLine}.
 */
@Service
public class ApprovalLineServiceImpl implements ApprovalLineService {

    private final Logger log = LoggerFactory.getLogger(ApprovalLineServiceImpl.class);

    private final ApprovalLineRepository approvalLineRepository;

    private final ApprovalLineMapper approvalLineMapper;

    public ApprovalLineServiceImpl(ApprovalLineRepository approvalLineRepository, ApprovalLineMapper approvalLineMapper) {
        this.approvalLineRepository = approvalLineRepository;
        this.approvalLineMapper = approvalLineMapper;
    }

    @Override
    public Mono<ApprovalLineDTO> save(ApprovalLineDTO approvalLineDTO) {
        log.debug("Request to save ApprovalLine : {}", approvalLineDTO);
        return approvalLineRepository.save(approvalLineMapper.toEntity(approvalLineDTO)).map(approvalLineMapper::toDto);
    }

    @Override
    public Mono<ApprovalLineDTO> update(ApprovalLineDTO approvalLineDTO) {
        log.debug("Request to update ApprovalLine : {}", approvalLineDTO);
        return approvalLineRepository.save(approvalLineMapper.toEntity(approvalLineDTO)).map(approvalLineMapper::toDto);
    }

    @Override
    public Mono<ApprovalLineDTO> partialUpdate(ApprovalLineDTO approvalLineDTO) {
        log.debug("Request to partially update ApprovalLine : {}", approvalLineDTO);

        return approvalLineRepository
            .findById(approvalLineDTO.getId())
            .map(existingApprovalLine -> {
                approvalLineMapper.partialUpdate(existingApprovalLine, approvalLineDTO);

                return existingApprovalLine;
            })
            .flatMap(approvalLineRepository::save)
            .map(approvalLineMapper::toDto);
    }

    @Override
    public Flux<ApprovalLineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApprovalLines");
        return approvalLineRepository.findAllBy(pageable).map(approvalLineMapper::toDto);
    }

    public Mono<Long> countAll() {
        return approvalLineRepository.count();
    }

    @Override
    public Mono<ApprovalLineDTO> findOne(String id) {
        log.debug("Request to get ApprovalLine : {}", id);
        return approvalLineRepository.findById(id).map(approvalLineMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ApprovalLine : {}", id);
        return approvalLineRepository.deleteById(id);
    }
}
