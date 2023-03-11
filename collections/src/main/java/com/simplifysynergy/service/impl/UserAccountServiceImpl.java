package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.UserAccount;
import com.simplifysynergy.repository.UserAccountRepository;
import com.simplifysynergy.service.UserAccountService;
import com.simplifysynergy.service.dto.UserAccountDTO;
import com.simplifysynergy.service.mapper.UserAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link UserAccount}.
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final Logger log = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    private final UserAccountRepository userAccountRepository;

    private final UserAccountMapper userAccountMapper;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, UserAccountMapper userAccountMapper) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountMapper = userAccountMapper;
    }

    @Override
    public Mono<UserAccountDTO> save(UserAccountDTO userAccountDTO) {
        log.debug("Request to save UserAccount : {}", userAccountDTO);
        return userAccountRepository.save(userAccountMapper.toEntity(userAccountDTO)).map(userAccountMapper::toDto);
    }

    @Override
    public Mono<UserAccountDTO> update(UserAccountDTO userAccountDTO) {
        log.debug("Request to update UserAccount : {}", userAccountDTO);
        return userAccountRepository.save(userAccountMapper.toEntity(userAccountDTO)).map(userAccountMapper::toDto);
    }

    @Override
    public Mono<UserAccountDTO> partialUpdate(UserAccountDTO userAccountDTO) {
        log.debug("Request to partially update UserAccount : {}", userAccountDTO);

        return userAccountRepository
            .findById(userAccountDTO.getId())
            .map(existingUserAccount -> {
                userAccountMapper.partialUpdate(existingUserAccount, userAccountDTO);

                return existingUserAccount;
            })
            .flatMap(userAccountRepository::save)
            .map(userAccountMapper::toDto);
    }

    @Override
    public Flux<UserAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAccounts");
        return userAccountRepository.findAllBy(pageable).map(userAccountMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userAccountRepository.count();
    }

    @Override
    public Mono<UserAccountDTO> findOne(String id) {
        log.debug("Request to get UserAccount : {}", id);
        return userAccountRepository.findById(id).map(userAccountMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete UserAccount : {}", id);
        return userAccountRepository.deleteById(id);
    }
}
