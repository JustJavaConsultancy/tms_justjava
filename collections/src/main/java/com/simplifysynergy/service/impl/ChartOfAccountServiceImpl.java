package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.ChartOfAccount;
import com.simplifysynergy.repository.ChartOfAccountRepository;
import com.simplifysynergy.service.ChartOfAccountService;
import com.simplifysynergy.service.dto.ChartOfAccountDTO;
import com.simplifysynergy.service.mapper.ChartOfAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ChartOfAccount}.
 */
@Service
public class ChartOfAccountServiceImpl implements ChartOfAccountService {

    private final Logger log = LoggerFactory.getLogger(ChartOfAccountServiceImpl.class);

    private final ChartOfAccountRepository chartOfAccountRepository;

    private final ChartOfAccountMapper chartOfAccountMapper;

    public ChartOfAccountServiceImpl(ChartOfAccountRepository chartOfAccountRepository, ChartOfAccountMapper chartOfAccountMapper) {
        this.chartOfAccountRepository = chartOfAccountRepository;
        this.chartOfAccountMapper = chartOfAccountMapper;
    }

    @Override
    public Mono<ChartOfAccountDTO> save(ChartOfAccountDTO chartOfAccountDTO) {
        log.debug("Request to save ChartOfAccount : {}", chartOfAccountDTO);
        return chartOfAccountRepository.save(chartOfAccountMapper.toEntity(chartOfAccountDTO)).map(chartOfAccountMapper::toDto);
    }

    @Override
    public Mono<ChartOfAccountDTO> update(ChartOfAccountDTO chartOfAccountDTO) {
        log.debug("Request to update ChartOfAccount : {}", chartOfAccountDTO);
        return chartOfAccountRepository.save(chartOfAccountMapper.toEntity(chartOfAccountDTO)).map(chartOfAccountMapper::toDto);
    }

    @Override
    public Mono<ChartOfAccountDTO> partialUpdate(ChartOfAccountDTO chartOfAccountDTO) {
        log.debug("Request to partially update ChartOfAccount : {}", chartOfAccountDTO);

        return chartOfAccountRepository
            .findById(chartOfAccountDTO.getId())
            .map(existingChartOfAccount -> {
                chartOfAccountMapper.partialUpdate(existingChartOfAccount, chartOfAccountDTO);

                return existingChartOfAccount;
            })
            .flatMap(chartOfAccountRepository::save)
            .map(chartOfAccountMapper::toDto);
    }

    @Override
    public Flux<ChartOfAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChartOfAccounts");
        return chartOfAccountRepository.findAllBy(pageable).map(chartOfAccountMapper::toDto);
    }

    public Mono<Long> countAll() {
        return chartOfAccountRepository.count();
    }

    @Override
    public Mono<ChartOfAccountDTO> findOne(String id) {
        log.debug("Request to get ChartOfAccount : {}", id);
        return chartOfAccountRepository.findById(id).map(chartOfAccountMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ChartOfAccount : {}", id);
        return chartOfAccountRepository.deleteById(id);
    }
}
