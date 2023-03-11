package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.Subscriber;
import com.simplifysynergy.repository.SubscriberRepository;
import com.simplifysynergy.service.SubscriberService;
import com.simplifysynergy.service.dto.SubscriberDTO;
import com.simplifysynergy.service.mapper.SubscriberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Subscriber}.
 */
@Service
public class SubscriberServiceImpl implements SubscriberService {

    private final Logger log = LoggerFactory.getLogger(SubscriberServiceImpl.class);

    private final SubscriberRepository subscriberRepository;

    private final SubscriberMapper subscriberMapper;

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository, SubscriberMapper subscriberMapper) {
        this.subscriberRepository = subscriberRepository;
        this.subscriberMapper = subscriberMapper;
    }

    @Override
    public Mono<SubscriberDTO> save(SubscriberDTO subscriberDTO) {
        log.debug("Request to save Subscriber : {}", subscriberDTO);
        return subscriberRepository.save(subscriberMapper.toEntity(subscriberDTO)).map(subscriberMapper::toDto);
    }

    @Override
    public Mono<SubscriberDTO> update(SubscriberDTO subscriberDTO) {
        log.debug("Request to update Subscriber : {}", subscriberDTO);
        return subscriberRepository.save(subscriberMapper.toEntity(subscriberDTO)).map(subscriberMapper::toDto);
    }

    @Override
    public Mono<SubscriberDTO> partialUpdate(SubscriberDTO subscriberDTO) {
        log.debug("Request to partially update Subscriber : {}", subscriberDTO);

        return subscriberRepository
            .findById(subscriberDTO.getId())
            .map(existingSubscriber -> {
                subscriberMapper.partialUpdate(existingSubscriber, subscriberDTO);

                return existingSubscriber;
            })
            .flatMap(subscriberRepository::save)
            .map(subscriberMapper::toDto);
    }

    @Override
    public Flux<SubscriberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Subscribers");
        return subscriberRepository.findAllBy(pageable).map(subscriberMapper::toDto);
    }

    public Mono<Long> countAll() {
        return subscriberRepository.count();
    }

    @Override
    public Mono<SubscriberDTO> findOne(String id) {
        log.debug("Request to get Subscriber : {}", id);
        return subscriberRepository.findById(id).map(subscriberMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Subscriber : {}", id);
        return subscriberRepository.deleteById(id);
    }
}
