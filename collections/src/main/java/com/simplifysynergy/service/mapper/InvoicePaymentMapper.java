package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.Invoice;
import com.simplifysynergy.domain.InvoicePayment;
import com.simplifysynergy.domain.Payer;
import com.simplifysynergy.domain.UserAccount;
import com.simplifysynergy.service.dto.InvoiceDTO;
import com.simplifysynergy.service.dto.InvoicePaymentDTO;
import com.simplifysynergy.service.dto.PayerDTO;
import com.simplifysynergy.service.dto.UserAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoicePayment} and its DTO {@link InvoicePaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoicePaymentMapper extends EntityMapper<InvoicePaymentDTO, InvoicePayment> {
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "invoiceId")
    @Mapping(target = "sourceAccount", source = "sourceAccount", qualifiedByName = "userAccountId")
    @Mapping(target = "destinationAccount", source = "destinationAccount", qualifiedByName = "userAccountId")
    @Mapping(target = "payer", source = "payer", qualifiedByName = "payerId")
    InvoicePaymentDTO toDto(InvoicePayment s);

    @Named("invoiceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InvoiceDTO toDtoInvoiceId(Invoice invoice);

    @Named("userAccountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserAccountDTO toDtoUserAccountId(UserAccount userAccount);

    @Named("payerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PayerDTO toDtoPayerId(Payer payer);
}
