package com.simplifysynergy.service.impl;

import com.simplifysynergy.domain.Notification;
import com.simplifysynergy.repository.NotificationRepository;
import com.simplifysynergy.service.NotificationService;
import com.simplifysynergy.service.dto.NotificationDTO;
import com.simplifysynergy.service.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Notification}.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public Mono<NotificationDTO> save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        return notificationRepository.save(notificationMapper.toEntity(notificationDTO)).map(notificationMapper::toDto);
    }

    @Override
    public Mono<NotificationDTO> update(NotificationDTO notificationDTO) {
        log.debug("Request to update Notification : {}", notificationDTO);
        return notificationRepository.save(notificationMapper.toEntity(notificationDTO)).map(notificationMapper::toDto);
    }

    @Override
    public Mono<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        log.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(existingNotification -> {
                notificationMapper.partialUpdate(existingNotification, notificationDTO);

                return existingNotification;
            })
            .flatMap(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    @Override
    public Flux<NotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAllBy(pageable).map(notificationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return notificationRepository.count();
    }

    @Override
    public Mono<NotificationDTO> findOne(String id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id).map(notificationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Notification : {}", id);
        return notificationRepository.deleteById(id);
    }
}
