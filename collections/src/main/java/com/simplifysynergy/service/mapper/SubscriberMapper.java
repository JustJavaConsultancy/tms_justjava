package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.Subscriber;
import com.simplifysynergy.service.dto.SubscriberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Subscriber} and its DTO {@link SubscriberDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriberMapper extends EntityMapper<SubscriberDTO, Subscriber> {}
