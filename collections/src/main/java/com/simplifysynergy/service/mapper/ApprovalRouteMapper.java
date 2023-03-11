package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.ApprovalLine;
import com.simplifysynergy.domain.ApprovalRoute;
import com.simplifysynergy.domain.RequestType;
import com.simplifysynergy.service.dto.ApprovalLineDTO;
import com.simplifysynergy.service.dto.ApprovalRouteDTO;
import com.simplifysynergy.service.dto.RequestTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApprovalRoute} and its DTO {@link ApprovalRouteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApprovalRouteMapper extends EntityMapper<ApprovalRouteDTO, ApprovalRoute> {
    @Mapping(target = "requestType", source = "requestType", qualifiedByName = "requestTypeType")
    @Mapping(target = "approvalLine", source = "approvalLine", qualifiedByName = "approvalLineId")
    ApprovalRouteDTO toDto(ApprovalRoute s);

    @Named("requestTypeType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    RequestTypeDTO toDtoRequestTypeType(RequestType requestType);

    @Named("approvalLineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApprovalLineDTO toDtoApprovalLineId(ApprovalLine approvalLine);
}
