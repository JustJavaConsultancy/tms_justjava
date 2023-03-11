package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.PaymentBatch;
import com.simplifysynergy.service.dto.PaymentBatchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentBatch} and its DTO {@link PaymentBatchDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentBatchMapper extends EntityMapper<PaymentBatchDTO, PaymentBatch> {}
