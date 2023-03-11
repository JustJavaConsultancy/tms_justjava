package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.UserSubscription;
import com.simplifysynergy.repository.UserSubscriptionRepository;
import com.simplifysynergy.service.UserSubscriptionService;
import com.simplifysynergy.service.dto.UserSubscriptionDTO;
import com.simplifysynergy.service.mapper.UserSubscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link UserSubscription}.
 */
@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(UserSubscriptionServiceImpl.class);

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final UserSubscriptionMapper userSubscriptionMapper;

    public UserSubscriptionServiceImpl(
        UserSubscriptionRepository userSubscriptionRepository,
        UserSubscriptionMapper userSubscriptionMapper
    ) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userSubscriptionMapper = userSubscriptionMapper;
    }

    @Override
    public Mono<UserSubscriptionDTO> save(UserSubscriptionDTO userSubscriptionDTO) {
        log.debug("Request to save UserSubscription : {}", userSubscriptionDTO);
        return userSubscriptionRepository.save(userSubscriptionMapper.toEntity(userSubscriptionDTO)).map(userSubscriptionMapper::toDto);
    }

    @Override
    public Mono<UserSubscriptionDTO> update(UserSubscriptionDTO userSubscriptionDTO) {
        log.debug("Request to update UserSubscription : {}", userSubscriptionDTO);
        return userSubscriptionRepository.save(userSubscriptionMapper.toEntity(userSubscriptionDTO)).map(userSubscriptionMapper::toDto);
    }

    @Override
    public Mono<UserSubscriptionDTO> partialUpdate(UserSubscriptionDTO userSubscriptionDTO) {
        log.debug("Request to partially update UserSubscription : {}", userSubscriptionDTO);

        return userSubscriptionRepository
            .findById(userSubscriptionDTO.getId())
            .map(existingUserSubscription -> {
                userSubscriptionMapper.partialUpdate(existingUserSubscription, userSubscriptionDTO);

                return existingUserSubscription;
            })
            .flatMap(userSubscriptionRepository::save)
            .map(userSubscriptionMapper::toDto);
    }

    @Override
    public Flux<UserSubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserSubscriptions");
        return userSubscriptionRepository.findAllBy(pageable).map(userSubscriptionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userSubscriptionRepository.count();
    }

    @Override
    public Mono<UserSubscriptionDTO> findOne(String id) {
        log.debug("Request to get UserSubscription : {}", id);
        return userSubscriptionRepository.findById(id).map(userSubscriptionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete UserSubscription : {}", id);
        return userSubscriptionRepository.deleteById(id);
    }
}
