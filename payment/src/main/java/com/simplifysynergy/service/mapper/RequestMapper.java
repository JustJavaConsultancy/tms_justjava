package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.Request;
import com.simplifysynergy.domain.RequestContent;
import com.simplifysynergy.service.dto.RequestContentDTO;
import com.simplifysynergy.service.dto.RequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Request} and its DTO {@link RequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestMapper extends EntityMapper<RequestDTO, Request> {
    @Mapping(target = "requestContent", source = "requestContent", qualifiedByName = "requestContentId")
    RequestDTO toDto(Request s);

    @Named("requestContentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RequestContentDTO toDtoRequestContentId(RequestContent requestContent);
}
