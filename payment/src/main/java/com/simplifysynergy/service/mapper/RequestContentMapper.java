package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.RequestContent;
import com.simplifysynergy.service.dto.RequestContentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RequestContent} and its DTO {@link RequestContentDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestContentMapper extends EntityMapper<RequestContentDTO, RequestContent> {}
