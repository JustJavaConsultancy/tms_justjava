package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.ServiceType;
import com.simplifysynergy.service.dto.ServiceTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceType} and its DTO {@link ServiceTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceTypeMapper extends EntityMapper<ServiceTypeDTO, ServiceType> {}
