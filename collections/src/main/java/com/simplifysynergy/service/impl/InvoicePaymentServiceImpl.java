package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.InvoicePayment;
import com.simplifysynergy.repository.InvoicePaymentRepository;
import com.simplifysynergy.service.InvoicePaymentService;
import com.simplifysynergy.service.dto.InvoicePaymentDTO;
import com.simplifysynergy.service.mapper.InvoicePaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link InvoicePayment}.
 */
@Service
public class InvoicePaymentServiceImpl implements InvoicePaymentService {

    private final Logger log = LoggerFactory.getLogger(InvoicePaymentServiceImpl.class);

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final InvoicePaymentMapper invoicePaymentMapper;

    public InvoicePaymentServiceImpl(InvoicePaymentRepository invoicePaymentRepository, InvoicePaymentMapper invoicePaymentMapper) {
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.invoicePaymentMapper = invoicePaymentMapper;
    }

    @Override
    public Mono<InvoicePaymentDTO> save(InvoicePaymentDTO invoicePaymentDTO) {
        log.debug("Request to save InvoicePayment : {}", invoicePaymentDTO);
        return invoicePaymentRepository.save(invoicePaymentMapper.toEntity(invoicePaymentDTO)).map(invoicePaymentMapper::toDto);
    }

    @Override
    public Mono<InvoicePaymentDTO> update(InvoicePaymentDTO invoicePaymentDTO) {
        log.debug("Request to update InvoicePayment : {}", invoicePaymentDTO);
        return invoicePaymentRepository.save(invoicePaymentMapper.toEntity(invoicePaymentDTO)).map(invoicePaymentMapper::toDto);
    }

    @Override
    public Mono<InvoicePaymentDTO> partialUpdate(InvoicePaymentDTO invoicePaymentDTO) {
        log.debug("Request to partially update InvoicePayment : {}", invoicePaymentDTO);

        return invoicePaymentRepository
            .findById(invoicePaymentDTO.getId())
            .map(existingInvoicePayment -> {
                invoicePaymentMapper.partialUpdate(existingInvoicePayment, invoicePaymentDTO);

                return existingInvoicePayment;
            })
            .flatMap(invoicePaymentRepository::save)
            .map(invoicePaymentMapper::toDto);
    }

    @Override
    public Flux<InvoicePaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoicePayments");
        return invoicePaymentRepository.findAllBy(pageable).map(invoicePaymentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return invoicePaymentRepository.count();
    }

    @Override
    public Mono<InvoicePaymentDTO> findOne(String id) {
        log.debug("Request to get InvoicePayment : {}", id);
        return invoicePaymentRepository.findById(id).map(invoicePaymentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete InvoicePayment : {}", id);
        return invoicePaymentRepository.deleteById(id);
    }
}
