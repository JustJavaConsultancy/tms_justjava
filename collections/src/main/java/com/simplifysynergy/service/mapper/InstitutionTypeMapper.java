package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.InstitutionType;
import com.simplifysynergy.service.dto.InstitutionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InstitutionType} and its DTO {@link InstitutionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface InstitutionTypeMapper extends EntityMapper<InstitutionTypeDTO, InstitutionType> {}
