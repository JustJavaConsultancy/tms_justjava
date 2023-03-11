package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.Receipt;
import com.simplifysynergy.repository.ReceiptRepository;
import com.simplifysynergy.service.ReceiptService;
import com.simplifysynergy.service.dto.ReceiptDTO;
import com.simplifysynergy.service.mapper.ReceiptMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Receipt}.
 */
@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final Logger log = LoggerFactory.getLogger(ReceiptServiceImpl.class);

    private final ReceiptRepository receiptRepository;

    private final ReceiptMapper receiptMapper;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
    }

    @Override
    public Mono<ReceiptDTO> save(ReceiptDTO receiptDTO) {
        log.debug("Request to save Receipt : {}", receiptDTO);
        return receiptRepository.save(receiptMapper.toEntity(receiptDTO)).map(receiptMapper::toDto);
    }

    @Override
    public Mono<ReceiptDTO> update(ReceiptDTO receiptDTO) {
        log.debug("Request to update Receipt : {}", receiptDTO);
        return receiptRepository.save(receiptMapper.toEntity(receiptDTO)).map(receiptMapper::toDto);
    }

    @Override
    public Mono<ReceiptDTO> partialUpdate(ReceiptDTO receiptDTO) {
        log.debug("Request to partially update Receipt : {}", receiptDTO);

        return receiptRepository
            .findById(receiptDTO.getId())
            .map(existingReceipt -> {
                receiptMapper.partialUpdate(existingReceipt, receiptDTO);

                return existingReceipt;
            })
            .flatMap(receiptRepository::save)
            .map(receiptMapper::toDto);
    }

    @Override
    public Flux<ReceiptDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Receipts");
        return receiptRepository.findAllBy(pageable).map(receiptMapper::toDto);
    }

    public Mono<Long> countAll() {
        return receiptRepository.count();
    }

    @Override
    public Mono<ReceiptDTO> findOne(String id) {
        log.debug("Request to get Receipt : {}", id);
        return receiptRepository.findById(id).map(receiptMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Receipt : {}", id);
        return receiptRepository.deleteById(id);
    }
}
