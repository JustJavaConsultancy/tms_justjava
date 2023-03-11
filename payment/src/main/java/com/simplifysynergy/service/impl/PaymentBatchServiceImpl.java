package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.PaymentBatch;
import com.simplifysynergy.repository.PaymentBatchRepository;
import com.simplifysynergy.service.PaymentBatchService;
import com.simplifysynergy.service.dto.PaymentBatchDTO;
import com.simplifysynergy.service.mapper.PaymentBatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link PaymentBatch}.
 */
@Service
public class PaymentBatchServiceImpl implements PaymentBatchService {

    private final Logger log = LoggerFactory.getLogger(PaymentBatchServiceImpl.class);

    private final PaymentBatchRepository paymentBatchRepository;

    private final PaymentBatchMapper paymentBatchMapper;

    public PaymentBatchServiceImpl(PaymentBatchRepository paymentBatchRepository, PaymentBatchMapper paymentBatchMapper) {
        this.paymentBatchRepository = paymentBatchRepository;
        this.paymentBatchMapper = paymentBatchMapper;
    }

    @Override
    public Mono<PaymentBatchDTO> save(PaymentBatchDTO paymentBatchDTO) {
        log.debug("Request to save PaymentBatch : {}", paymentBatchDTO);
        return paymentBatchRepository.save(paymentBatchMapper.toEntity(paymentBatchDTO)).map(paymentBatchMapper::toDto);
    }

    @Override
    public Mono<PaymentBatchDTO> update(PaymentBatchDTO paymentBatchDTO) {
        log.debug("Request to update PaymentBatch : {}", paymentBatchDTO);
        return paymentBatchRepository.save(paymentBatchMapper.toEntity(paymentBatchDTO)).map(paymentBatchMapper::toDto);
    }

    @Override
    public Mono<PaymentBatchDTO> partialUpdate(PaymentBatchDTO paymentBatchDTO) {
        log.debug("Request to partially update PaymentBatch : {}", paymentBatchDTO);

        return paymentBatchRepository
            .findById(paymentBatchDTO.getId())
            .map(existingPaymentBatch -> {
                paymentBatchMapper.partialUpdate(existingPaymentBatch, paymentBatchDTO);

                return existingPaymentBatch;
            })
            .flatMap(paymentBatchRepository::save)
            .map(paymentBatchMapper::toDto);
    }

    @Override
    public Flux<PaymentBatchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentBatches");
        return paymentBatchRepository.findAllBy(pageable).map(paymentBatchMapper::toDto);
    }

    public Mono<Long> countAll() {
        return paymentBatchRepository.count();
    }

    @Override
    public Mono<PaymentBatchDTO> findOne(String id) {
        log.debug("Request to get PaymentBatch : {}", id);
        return paymentBatchRepository.findById(id).map(paymentBatchMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete PaymentBatch : {}", id);
        return paymentBatchRepository.deleteById(id);
    }
}
