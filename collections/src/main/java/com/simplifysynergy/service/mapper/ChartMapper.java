package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.Chart;
import com.simplifysynergy.domain.ChartOfAccount;
import com.simplifysynergy.service.dto.ChartDTO;
import com.simplifysynergy.service.dto.ChartOfAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Chart} and its DTO {@link ChartDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChartMapper extends EntityMapper<ChartDTO, Chart> {
    @Mapping(target = "chartOfAccount", source = "chartOfAccount", qualifiedByName = "chartOfAccountId")
    ChartDTO toDto(Chart s);

    @Named("chartOfAccountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChartOfAccountDTO toDtoChartOfAccountId(ChartOfAccount chartOfAccount);
}
