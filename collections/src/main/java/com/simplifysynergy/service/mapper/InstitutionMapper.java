package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.ApprovalRoute;
import com.simplifysynergy.domain.ChartOfAccount;
import com.simplifysynergy.domain.Institution;
import com.simplifysynergy.domain.InstitutionType;
import com.simplifysynergy.service.dto.ApprovalRouteDTO;
import com.simplifysynergy.service.dto.ChartOfAccountDTO;
import com.simplifysynergy.service.dto.InstitutionDTO;
import com.simplifysynergy.service.dto.InstitutionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Institution} and its DTO {@link InstitutionDTO}.
 */
@Mapper(componentModel = "spring")
public interface InstitutionMapper extends EntityMapper<InstitutionDTO, Institution> {
    @Mapping(target = "chartOfAccount", source = "chartOfAccount", qualifiedByName = "chartOfAccountId")
    @Mapping(target = "approvalRoute", source = "approvalRoute", qualifiedByName = "approvalRouteId")
    @Mapping(target = "institutionType", source = "institutionType", qualifiedByName = "institutionTypeId")
    InstitutionDTO toDto(Institution s);

    @Named("chartOfAccountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChartOfAccountDTO toDtoChartOfAccountId(ChartOfAccount chartOfAccount);

    @Named("approvalRouteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApprovalRouteDTO toDtoApprovalRouteId(ApprovalRoute approvalRoute);

    @Named("institutionTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InstitutionTypeDTO toDtoInstitutionTypeId(InstitutionType institutionType);
}
