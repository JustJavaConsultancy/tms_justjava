package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.ChartOfAccount;
import com.simplifysynergy.service.dto.ChartOfAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChartOfAccount} and its DTO {@link ChartOfAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChartOfAccountMapper extends EntityMapper<ChartOfAccountDTO, ChartOfAccount> {}
