package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.Invoice;
import com.simplifysynergy.repository.InvoiceRepository;
import com.simplifysynergy.service.InvoiceService;
import com.simplifysynergy.service.dto.InvoiceDTO;
import com.simplifysynergy.service.mapper.InvoiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Invoice}.
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public Mono<InvoiceDTO> save(InvoiceDTO invoiceDTO) {
        log.debug("Request to save Invoice : {}", invoiceDTO);
        return invoiceRepository.save(invoiceMapper.toEntity(invoiceDTO)).map(invoiceMapper::toDto);
    }

    @Override
    public Mono<InvoiceDTO> update(InvoiceDTO invoiceDTO) {
        log.debug("Request to update Invoice : {}", invoiceDTO);
        return invoiceRepository.save(invoiceMapper.toEntity(invoiceDTO)).map(invoiceMapper::toDto);
    }

    @Override
    public Mono<InvoiceDTO> partialUpdate(InvoiceDTO invoiceDTO) {
        log.debug("Request to partially update Invoice : {}", invoiceDTO);

        return invoiceRepository
            .findById(invoiceDTO.getId())
            .map(existingInvoice -> {
                invoiceMapper.partialUpdate(existingInvoice, invoiceDTO);

                return existingInvoice;
            })
            .flatMap(invoiceRepository::save)
            .map(invoiceMapper::toDto);
    }

    @Override
    public Flux<InvoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAllBy(pageable).map(invoiceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return invoiceRepository.count();
    }

    @Override
    public Mono<InvoiceDTO> findOne(String id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id).map(invoiceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Invoice : {}", id);
        return invoiceRepository.deleteById(id);
    }
}
