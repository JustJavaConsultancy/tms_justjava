package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.Payer;
import com.simplifysynergy.repository.PayerRepository;
import com.simplifysynergy.service.PayerService;
import com.simplifysynergy.service.dto.PayerDTO;
import com.simplifysynergy.service.mapper.PayerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Payer}.
 */
@Service
public class PayerServiceImpl implements PayerService {

    private final Logger log = LoggerFactory.getLogger(PayerServiceImpl.class);

    private final PayerRepository payerRepository;

    private final PayerMapper payerMapper;

    public PayerServiceImpl(PayerRepository payerRepository, PayerMapper payerMapper) {
        this.payerRepository = payerRepository;
        this.payerMapper = payerMapper;
    }

    @Override
    public Mono<PayerDTO> save(PayerDTO payerDTO) {
        log.debug("Request to save Payer : {}", payerDTO);
        return payerRepository.save(payerMapper.toEntity(payerDTO)).map(payerMapper::toDto);
    }

    @Override
    public Mono<PayerDTO> update(PayerDTO payerDTO) {
        log.debug("Request to update Payer : {}", payerDTO);
        return payerRepository.save(payerMapper.toEntity(payerDTO)).map(payerMapper::toDto);
    }

    @Override
    public Mono<PayerDTO> partialUpdate(PayerDTO payerDTO) {
        log.debug("Request to partially update Payer : {}", payerDTO);

        return payerRepository
            .findById(payerDTO.getId())
            .map(existingPayer -> {
                payerMapper.partialUpdate(existingPayer, payerDTO);

                return existingPayer;
            })
            .flatMap(payerRepository::save)
            .map(payerMapper::toDto);
    }

    @Override
    public Flux<PayerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payers");
        return payerRepository.findAllBy(pageable).map(payerMapper::toDto);
    }

    public Mono<Long> countAll() {
        return payerRepository.count();
    }

    @Override
    public Mono<PayerDTO> findOne(String id) {
        log.debug("Request to get Payer : {}", id);
        return payerRepository.findById(id).map(payerMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Payer : {}", id);
        return payerRepository.deleteById(id);
    }
}
