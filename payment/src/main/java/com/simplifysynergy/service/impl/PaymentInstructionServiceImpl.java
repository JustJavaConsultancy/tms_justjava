package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.PaymentInstruction;
import com.simplifysynergy.repository.PaymentInstructionRepository;
import com.simplifysynergy.service.PaymentInstructionService;
import com.simplifysynergy.service.dto.PaymentInstructionDTO;
import com.simplifysynergy.service.mapper.PaymentInstructionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link PaymentInstruction}.
 */
@Service
public class PaymentInstructionServiceImpl implements PaymentInstructionService {

    private final Logger log = LoggerFactory.getLogger(PaymentInstructionServiceImpl.class);

    private final PaymentInstructionRepository paymentInstructionRepository;

    private final PaymentInstructionMapper paymentInstructionMapper;

    public PaymentInstructionServiceImpl(
        PaymentInstructionRepository paymentInstructionRepository,
        PaymentInstructionMapper paymentInstructionMapper
    ) {
        this.paymentInstructionRepository = paymentInstructionRepository;
        this.paymentInstructionMapper = paymentInstructionMapper;
    }

    @Override
    public Mono<PaymentInstructionDTO> save(PaymentInstructionDTO paymentInstructionDTO) {
        log.debug("Request to save PaymentInstruction : {}", paymentInstructionDTO);
        return paymentInstructionRepository
            .save(paymentInstructionMapper.toEntity(paymentInstructionDTO))
            .map(paymentInstructionMapper::toDto);
    }

    @Override
    public Mono<PaymentInstructionDTO> update(PaymentInstructionDTO paymentInstructionDTO) {
        log.debug("Request to update PaymentInstruction : {}", paymentInstructionDTO);
        return paymentInstructionRepository
            .save(paymentInstructionMapper.toEntity(paymentInstructionDTO))
            .map(paymentInstructionMapper::toDto);
    }

    @Override
    public Mono<PaymentInstructionDTO> partialUpdate(PaymentInstructionDTO paymentInstructionDTO) {
        log.debug("Request to partially update PaymentInstruction : {}", paymentInstructionDTO);

        return paymentInstructionRepository
            .findById(paymentInstructionDTO.getId())
            .map(existingPaymentInstruction -> {
                paymentInstructionMapper.partialUpdate(existingPaymentInstruction, paymentInstructionDTO);

                return existingPaymentInstruction;
            })
            .flatMap(paymentInstructionRepository::save)
            .map(paymentInstructionMapper::toDto);
    }

    @Override
    public Flux<PaymentInstructionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentInstructions");
        return paymentInstructionRepository.findAllBy(pageable).map(paymentInstructionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return paymentInstructionRepository.count();
    }

    @Override
    public Mono<PaymentInstructionDTO> findOne(String id) {
        log.debug("Request to get PaymentInstruction : {}", id);
        return paymentInstructionRepository.findById(id).map(paymentInstructionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete PaymentInstruction : {}", id);
        return paymentInstructionRepository.deleteById(id);
    }
}
