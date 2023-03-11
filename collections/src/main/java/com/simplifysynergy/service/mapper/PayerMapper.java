package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.Payer;
import com.simplifysynergy.service.dto.PayerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payer} and its DTO {@link PayerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PayerMapper extends EntityMapper<PayerDTO, Payer> {}
