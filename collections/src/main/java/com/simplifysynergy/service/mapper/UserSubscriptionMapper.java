package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.CollectionService;
import com.simplifysynergy.domain.Subscriber;
import com.simplifysynergy.domain.UserSubscription;
import com.simplifysynergy.service.dto.CollectionServiceDTO;
import com.simplifysynergy.service.dto.SubscriberDTO;
import com.simplifysynergy.service.dto.UserSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSubscription} and its DTO {@link UserSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSubscriptionMapper extends EntityMapper<UserSubscriptionDTO, UserSubscription> {
    @Mapping(target = "service", source = "service", qualifiedByName = "collectionServiceId")
    @Mapping(target = "subscriber", source = "subscriber", qualifiedByName = "subscriberId")
    UserSubscriptionDTO toDto(UserSubscription s);

    @Named("collectionServiceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CollectionServiceDTO toDtoCollectionServiceId(CollectionService collectionService);

    @Named("subscriberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriberDTO toDtoSubscriberId(Subscriber subscriber);
}
