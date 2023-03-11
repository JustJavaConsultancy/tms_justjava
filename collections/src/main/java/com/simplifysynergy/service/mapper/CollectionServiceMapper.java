package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.CollectionService;
import com.simplifysynergy.domain.ServiceType;
import com.simplifysynergy.service.dto.CollectionServiceDTO;
import com.simplifysynergy.service.dto.ServiceTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CollectionService} and its DTO {@link CollectionServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CollectionServiceMapper extends EntityMapper<CollectionServiceDTO, CollectionService> {
    @Mapping(target = "serviceType", source = "serviceType", qualifiedByName = "serviceTypeId")
    CollectionServiceDTO toDto(CollectionService s);

    @Named("serviceTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServiceTypeDTO toDtoServiceTypeId(ServiceType serviceType);
}
