package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.Chart;
import com.simplifysynergy.repository.ChartRepository;
import com.simplifysynergy.service.ChartService;
import com.simplifysynergy.service.dto.ChartDTO;
import com.simplifysynergy.service.mapper.ChartMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Chart}.
 */
@Service
public class ChartServiceImpl implements ChartService {

    private final Logger log = LoggerFactory.getLogger(ChartServiceImpl.class);

    private final ChartRepository chartRepository;

    private final ChartMapper chartMapper;

    public ChartServiceImpl(ChartRepository chartRepository, ChartMapper chartMapper) {
        this.chartRepository = chartRepository;
        this.chartMapper = chartMapper;
    }

    @Override
    public Mono<ChartDTO> save(ChartDTO chartDTO) {
        log.debug("Request to save Chart : {}", chartDTO);
        return chartRepository.save(chartMapper.toEntity(chartDTO)).map(chartMapper::toDto);
    }

    @Override
    public Mono<ChartDTO> update(ChartDTO chartDTO) {
        log.debug("Request to update Chart : {}", chartDTO);
        return chartRepository.save(chartMapper.toEntity(chartDTO)).map(chartMapper::toDto);
    }

    @Override
    public Mono<ChartDTO> partialUpdate(ChartDTO chartDTO) {
        log.debug("Request to partially update Chart : {}", chartDTO);

        return chartRepository
            .findById(chartDTO.getId())
            .map(existingChart -> {
                chartMapper.partialUpdate(existingChart, chartDTO);

                return existingChart;
            })
            .flatMap(chartRepository::save)
            .map(chartMapper::toDto);
    }

    @Override
    public Flux<ChartDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Charts");
        return chartRepository.findAllBy(pageable).map(chartMapper::toDto);
    }

    public Mono<Long> countAll() {
        return chartRepository.count();
    }

    @Override
    public Mono<ChartDTO> findOne(String id) {
        log.debug("Request to get Chart : {}", id);
        return chartRepository.findById(id).map(chartMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Chart : {}", id);
        return chartRepository.deleteById(id);
    }
}
