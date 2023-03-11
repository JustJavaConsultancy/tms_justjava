package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.PaymentBatch;
import com.simplifysynergy.domain.PaymentInstruction;
import com.simplifysynergy.service.dto.PaymentBatchDTO;
import com.simplifysynergy.service.dto.PaymentInstructionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentInstruction} and its DTO {@link PaymentInstructionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentInstructionMapper extends EntityMapper<PaymentInstructionDTO, PaymentInstruction> {
    @Mapping(target = "paymentBatch", source = "paymentBatch", qualifiedByName = "paymentBatchId")
    PaymentInstructionDTO toDto(PaymentInstruction s);

    @Named("paymentBatchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentBatchDTO toDtoPaymentBatchId(PaymentBatch paymentBatch);
}
