package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.RequestType;
import com.simplifysynergy.service.dto.RequestTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RequestType} and its DTO {@link RequestTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestTypeMapper extends EntityMapper<RequestTypeDTO, RequestType> {}
